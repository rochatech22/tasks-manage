package com.taskmanager.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret:mySecretKey}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}") // 24 horas em millisegundos
    private Long expiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * Extrai o username (email) do token
     * @param token Token JWT
     * @return Username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrai a data de expiração do token
     * @param token Token JWT
     * @return Data de expiração
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extrai um claim específico do token
     * @param token Token JWT
     * @param claimsResolver Função para extrair o claim
     * @return Valor do claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extrai todos os claims do token
     * @param token Token JWT
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Verifica se o token está expirado
     * @param token Token JWT
     * @return true se expirado, false caso contrário
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Gera um token JWT para o usuário
     * @param userDetails Detalhes do usuário
     * @return Token JWT
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Gera um token JWT com claims customizados
     * @param userDetails Detalhes do usuário
     * @param userId ID do usuário
     * @param name Nome do usuário
     * @return Token JWT
     */
    public String generateToken(UserDetails userDetails, Long userId, String name) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("name", name);
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Cria o token JWT
     * @param claims Claims do token
     * @param subject Subject (username/email)
     * @return Token JWT
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Valida o token JWT
     * @param token Token JWT
     * @param userDetails Detalhes do usuário
     * @return true se válido, false caso contrário
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Extrai o ID do usuário do token
     * @param token Token JWT
     * @return ID do usuário
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        } else if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        }
        return null;
    }
    
    /**
     * Extrai o nome do usuário do token
     * @param token Token JWT
     * @return Nome do usuário
     */
    public String extractName(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("name");
    }
    
    /**
     * Verifica se o token é válido (não expirado e bem formado)
     * @param token Token JWT
     * @return true se válido, false caso contrário
     */
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

