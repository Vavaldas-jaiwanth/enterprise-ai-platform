package com.enterprise.ai.platform.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enterprise.ai.platform.Model.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:600000}")
    private Long jwtExpirationMs;

    public SecretKey generaSecretKey()
    {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)); 
    }

    public String generateToken(CustomUserDetails user)
    {
        Map<String,Object> claims=new HashMap<>();

        claims.put("id",user.getId());

        return Jwts.builder()
                    .subject(user.getUsername())
                    .claims(claims)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(generaSecretKey())
                    .compact();
    }

    public long getExpirationTime() {
        return jwtExpirationMs;
    }

    public String extractUsername(String token)
    {
        return extractClaim(token,Claims::getSubject );
    }

    public Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                    .verifyWith(generaSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
    public <T> T extractClaim(String token,Function<Claims,T> claimsResolver)
    {
        final Claims claims=extractAllClaims(token);
        
        return claimsResolver.apply(claims);
    }
    public Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token,String username)
    {
        final String jwtusername=extractUsername(token);

        return (jwtusername.equals(username)) && (!isTokenExpired(token));
    }

}
