package com.jzsk.backendv2.security;

import com.jzsk.backendv2.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private final SecurityProperties securityProperties;

    public String generateToken(LoginUser loginUser) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + securityProperties.getExpireSeconds() * 1000L);
        return Jwts.builder()
                .setSubject(loginUser.getUsername())
                .claim("userId", loginUser.getUserId())
                .claim("displayName", loginUser.getDisplayName())
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(SignatureAlgorithm.HS512, securityProperties.getSecret())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(securityProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(securityProperties.getHeader());
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        String tokenPrefix = securityProperties.getTokenPrefix();
        if (StringUtils.hasText(tokenPrefix) && authorization.startsWith(tokenPrefix)) {
            return authorization.substring(tokenPrefix.length());
        }
        return authorization;
    }
}
