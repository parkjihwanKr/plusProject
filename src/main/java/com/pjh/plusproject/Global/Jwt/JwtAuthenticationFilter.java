package com.pjh.plusproject.Global.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjh.plusproject.Global.DTO.TokenDTO;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import com.pjh.plusproject.Member.DTO.LoginDTO;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication()");
        log.info("request : "+request);
        try {
            LoginDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("successfulAuthentication()");
        String username = ((MemberDetailsImpl) authResult.getPrincipal()).getUsername();
        MemberRoleEnum role = ((MemberDetailsImpl) authResult.getPrincipal()).getMember().getRole();

        TokenDTO token = jwtProvider.createToken(username, role);
        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());
        response.setHeader(JwtProvider.REFRESH_TOKEN_HEADER, token.getRefreshToken());
        // response.setHeader(); 없을 때 넣어주는데, 중복된 토큰이 있으면 업데이트 해준다.
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
