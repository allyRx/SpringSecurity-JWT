package com.vasia.authenticationwithspring.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private  long refreshExpiration;


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllclaims(token);
        return claimsResolver.apply(claims);
    }

    //Extraction de toutes les revendications (claims).
    private Claims extractAllclaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Récupération de la clé de signature
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //generation de token
    public String generateToken(UserDetails userDetails){
        return createToken(new HashMap<>(),userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return createToken(new HashMap<>(), userDetails,refreshExpiration);
    }

    private String createToken(Map<String, Object> extractClaims, UserDetails userDetails, long jwtExpiration) {
        return
                Jwts
                        .builder()
                        .setClaims(extractClaims)
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                        .compact();

    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final  String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
