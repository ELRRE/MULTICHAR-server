package com.example.multichar.interceptors;

import com.example.multichar.repositories.TokenRepository;
import com.example.multichar.service.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    final JwtProvider jwtProvider;
    final TokenRepository tokenRepository;
    @Autowired
    public AuthenticationInterceptor(JwtProvider jwtProvider, TokenRepository tokenRepository) {
        this.jwtProvider = jwtProvider;
        this.tokenRepository = tokenRepository;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try{
            String accessToken = request.getHeader("Token").replace("Bearer ","");
            if(!tokenRepository.existsByAccessToken(accessToken))
                throw new EntityNotFoundException("Token expired");
            return jwtProvider.validateAccessToken(accessToken);
        } catch (Exception e){
            response.getWriter().write(e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

}
