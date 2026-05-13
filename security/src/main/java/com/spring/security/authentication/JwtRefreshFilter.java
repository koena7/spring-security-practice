package com.spring.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtRefreshFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JwtRefreshFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-----------INSIDE Refresh Filter------------");

        if(!request.getServletPath().equals("/refresh-token")){
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractRefreshToken(request);

        if(refreshToken == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(refreshToken);
        Authentication authResult = authenticationManager.authenticate(authenticationToken);

        if(authResult.isAuthenticated()){
            String newAccessToken = jwtUtil.generateToken(authResult.getName(), 5);
            response.setHeader("Authorization", "Bearer "+newAccessToken);
        }
    }

    private String extractRefreshToken(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }

        for(Cookie cookie : cookies){
            if((cookie.getName()).equals("RefreshToken")){
                log.info("Found the refresh cookie");
                token = cookie.getValue();
            }
        }
        log.info("Refrsh Token: {}", token);
        return token;
    }
}
