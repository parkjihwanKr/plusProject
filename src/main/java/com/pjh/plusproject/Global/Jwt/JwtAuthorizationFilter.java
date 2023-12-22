package com.pjh.plusproject.Global.Jwt;

import com.pjh.plusproject.Global.Security.MemberDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberDetailsServiceImpl memberDetailsService;

    public JwtAuthorizationFilter(JwtProvider jwtProvider, MemberDetailsServiceImpl memberDetailsService) {
        this.jwtProvider = jwtProvider;
        this.memberDetailsService = memberDetailsService;
    }
    // 인증 처리
    public void setAuthentication(String username) {
        log.info("setAuthentication()");
        // 새로운 시큐리티 context를 만듦
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // SecurityContextHolder안에 존재하는 Authentication에 createAuthentication(username)을 하여
        // 해당 인증 객체에 username에 넣어줍니다.
        Authentication authentication = createAuthentication(username);
        // contextHolder에 인증 객체를 넣어줌
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        log.info("createAuthentication()");
        UserDetails userDetails = memberDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    // 안될 수도 있어서 일단은 가지고 있기
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal()");
        String tokenValue = jwtProvider.getJwtFromHeader(req);
        log.info("tokenValue : "+tokenValue);
        // 여기 null
        if (StringUtils.hasText(tokenValue)) {
            if (!jwtProvider.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }
            Claims info = jwtProvider.getUserInfoFromToken(tokenValue);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        filterChain.doFilter(req, res);
    }
}