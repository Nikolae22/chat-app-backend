package com.chatapp.auth.security.jwt;

import com.chatapp.auth.users.User;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;


    public String generateToken(User user){
        return Jwts.builder()
                .claim("userId",user.getId())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String getUsernameFromToken(String token){
        return getClaimsForToken(token)
                .getSubject();
    }

    public boolean isValidToken(String token,User user){
        final String username=getUsernameFromToken(token);
        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return getClaimsForToken(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims getClaimsForToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            // try hex decoding (tests use a hex string)
            byte[] keyBytes = java.util.HexFormat.of().parseHex(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }


}
