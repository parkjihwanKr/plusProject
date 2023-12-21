package com.pjh.plusproject.Global.Jwt;

import com.pjh.plusproject.Global.Config.JwtProperties;
import com.pjh.plusproject.Global.DTO.TokenDTO;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.Cookie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Getter
@Slf4j
@Component
public class JwtProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    // private static final long TOKEN_TIME = 60 * 60 * 1000L;
    private JwtProperties jwtProperties;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private String secretKey;
    private Key key;

    @Autowired
    public JwtProvider(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init(){
        log.info("init start!");
        secretKey = jwtProperties.getSecretKey();
        accessTokenExpiration = jwtProperties.getAccessTokenExpiration();
        refreshTokenExpiration = jwtProperties.getRefreshTokenExpiration();
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    public TokenDTO createToken(String username, MemberRoleEnum role){
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+refreshTokenExpiration))
                .signWith(key, signatureAlgorithm)
                .compact();

        return TokenDTO.of(accessToken, refreshToken);
    }

    public void setTokenForCookie(TokenDTO tokenDTO, HttpServletResponse respnose){
        String accessToken = URLEncoder.encode(BEARER_PREFIX + tokenDTO.getAccessToken(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String refreshToken = URLEncoder.encode(BEARER_PREFIX + tokenDTO.getRefreshToken(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        makeTokenCookie(AUTHORIZATION_HEADER, accessToken);
        makeTokenCookie(REFRESH_TOKEN_HEADER, refreshToken);
    }

    private Cookie makeTokenCookie(String header, String value){
        Cookie cookie = new Cookie(header, value);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }

    public boolean validateToken(String token) {
        log.info("token : "+ token);
        try {
            log.info("validateToken");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /*public String getTokenFromCookie(String header, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> header.equals(cookie.getName()))
                .findFirst()
                .map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
                .map(this::substringToken)
                .orElse("");
    }*/

    private String substringToken(String tokenValue) {
        log.info("tokenValue : "+tokenValue);
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }

        log.error("Can not Substring Token Value");
        throw new IllegalArgumentException();
    }

    // JWT 사용자 정보를 가져오기
    public Claims getUserInfoFromToken(String token) {
        log.info("getUserInfoFromToken");
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getJwtFromHeader(HttpServletRequest req){
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getAuthorizationHeader(){
        return AUTHORIZATION_HEADER;
    }

    public String getRefreshTokenHeader(){
        return REFRESH_TOKEN_HEADER;
    }
}
