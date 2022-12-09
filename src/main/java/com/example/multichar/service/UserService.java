package com.example.multichar.service;

import com.example.multichar.Entity.*;
import com.example.multichar.advices.TokenException;
import com.example.multichar.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.security.auth.message.AuthException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final SCryptPasswordEncoder sCryptPasswordEncoder = SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8();
    @Autowired
    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public User getInfo(String username){
        return userRepository.findByName(username).orElseThrow(()->new EntityNotFoundException("user not found"));
    }
    public Tokens registration(User user, String passConfirmation)
    {
        if (!user.getPass().equals(passConfirmation))
            throw new SecurityException("password not confirmed");
        if (userRepository.existsByEmailOrName(user.getEmail(),user.getName()))
            throw new EntityExistsException("User with this name or email already exists");
        user.setPass(sCryptPasswordEncoder.encode(user.getPass()));//шифрование пароля
        Tokens tokens = new Tokens();
        tokens.setAccessToken(jwtProvider.generateAccessToken(user));
        tokens.setRefreshToken(jwtProvider.generateRefreshToken(user));
        user.setTokens(tokens);
        userRepository.save(user);

        return tokens;
    }
    /*
    * user authorization using username or email.
    * Returns access and refresh tokens
    *
    * @param loginName username or user's email
    * @param pass  user's password
    * @return      access and refresh tokens
    * */
    public Tokens login(String loginName, String pass){
        User user = userRepository.findByEmailOrName(loginName, loginName).orElseThrow(()->new EntityNotFoundException("user not found"));
        if(!sCryptPasswordEncoder.matches(pass,user.getPass()))
            throw new SecurityException("invalid password");
        user.getTokens().setRefreshToken(jwtProvider.generateRefreshToken(user));
        user.getTokens().setAccessToken(jwtProvider.generateAccessToken(user));
        userRepository.save(user);
        return user.getTokens();
    }
    public Tokens refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims =jwtProvider.getRefreshClaims(refreshToken);
            String username = claims.getSubject();
            User user = userRepository.findByName(username)
                    .orElseThrow(()->new EntityNotFoundException("User not found"));
            String saveRefreshToken = user
                    .getTokens()
                    .getRefreshToken();
            if(saveRefreshToken != null && saveRefreshToken.equals(refreshToken)){
                user.getTokens().setRefreshToken(jwtProvider.generateRefreshToken(user));
                user.getTokens().setAccessToken(jwtProvider.generateAccessToken(user));
                userRepository.save(user);
                return user.getTokens();
            }
        }
        throw new TokenException("Invalid JWT token");
    }
    /*
    * logout using email or username.
    * */
    public void logout(String username){
        User user = userRepository.findByName(username)
                .orElseThrow(()->new EntityNotFoundException("пользователь не найден"));
        user.getTokens().setAccessToken(null);
        user.getTokens().setRefreshToken(null);
        userRepository.save(user);
    }



}
