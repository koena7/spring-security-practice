package com.spring.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {

    AuthenticationManager authenticationManager;

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("----------INSIDE Validation Filter------------");

        String token = extractTokenFromRequest(request);
        log.info("Your token is: {}",token);
        if(token != null){
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);
            Authentication authResult = authenticationManager.authenticate(authToken);
            if(authResult.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authResult);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request){
        String tokenString = request.getHeader("Authorization");
        if(tokenString != null){
            return tokenString.substring(7);
        }
        return null;
    }
}
