package com.jzsk.backendv2.security;

import com.jzsk.backendv2.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final DatabaseUserDetailsService databaseUserDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final SecurityPathProvider securityPathProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return securityPathProvider.isPermitAll(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenService.resolveToken(request);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtTokenService.parseToken(token);
            String username = claims.getSubject();
            LoginUser loginUser = (LoginUser) databaseUserDetailsService.loadUserByUsername(username);

            JwtUserAuthenticationToken authentication = new JwtUserAuthenticationToken(
                    loginUser,
                    loginUser.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            restAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException(ErrorCode.TOKEN_INVALID.getMessage(), ex)
            );
        }
    }
}
