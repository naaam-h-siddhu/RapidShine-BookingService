package com.rapidshine.carwash.bookingservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class temp {
    public static void main(String[] args) {
        String secret = "a-string-secret-at-least-256-bits-long-siddhuertgwerasgagas";
        long nowMillis = System.currentTimeMillis();

        String token = Jwts.builder()
                .setSubject("booking-service")
                .claim("role", new String[]{"SERVICE"})
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + 9999_99_999)) // 1 hour
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS384)
                .compact();

        System.out.println("JWT Token:\n" + token);
    }
}