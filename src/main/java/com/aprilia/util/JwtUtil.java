package com.aprilia.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "aprilia";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;
    public String generateToken(Long userId,String username, String  role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
    public Long getUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) return null;
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    public String getUserRoleFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) return null;
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }
//    获取用户名
    public String getUsernameFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) return null;
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // 去掉 "Bearer "
        }
        return null;
    }
}
