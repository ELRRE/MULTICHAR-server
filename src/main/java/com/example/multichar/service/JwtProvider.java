package com.example.multichar.service;

import com.example.multichar.Entity.User;
import com.example.multichar.advices.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }
    public String generateAccessToken(@NonNull User user){
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusDays(30)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        final Date accessExpiration =Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getName())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("email",user.getEmail())
                .compact();
    }
    public String generateRefreshToken(@NonNull User user){
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusDays(30)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        final Date accessExpiration =Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getName())
                .setExpiration(accessExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }
    public boolean validateAccessToken(@NonNull String accessToken){
        return validateToken(accessToken,jwtAccessSecret);
    }
    public boolean validateRefreshToken(@NonNull String refreshToken){
        return validateToken(refreshToken,jwtRefreshSecret);
    }
    private boolean validateToken(@NonNull String token, @NonNull Key secret){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token expired");
        } catch (UnsupportedJwtException e) {
            throw new TokenException("Unsupported jwt");
        } catch (MalformedJwtException e) {
            throw new TokenException("Malformed jwt");
        } catch (SignatureException e) {
            throw new TokenException("Invalid signature");
        } catch (IllegalArgumentException e) {
            throw new TokenException("invalid token");
        }
    }
    public Claims getAccessClaims(@NonNull String token){
        return getClaims(token, jwtAccessSecret);
    }
    public String getAccessSubject(@NonNull String token){
        return  getClaims(token.replace("Bearer ",""),jwtAccessSecret).getSubject();
    }
    public String getRefreshSubject(@NonNull String token){
        return  getClaims(token.replace("Bearer ",""),jwtRefreshSecret).getSubject();
    }
    public Claims getRefreshClaims(@NonNull String token){
        return  getClaims(token,jwtRefreshSecret);
    }
    private Claims getClaims(@NonNull String token, @NonNull Key secret){
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
