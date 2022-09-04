package com.lofserver.soma.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;

public class JsonWebToken {
    public String makeJwtTokenById(Long id) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // header type (jwt)
                .setIssuer("lof-server") // (iss setting)
                .setIssuedAt(now) // (iat setting)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // (exp setting)
                .claim("id", id) // (secret claim setting)
                .signWith(SignatureAlgorithm.HS256, "secret") // (signature setting)
                .compact();
    }

    public Claims parseJwtToken(String token){
        return Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(token)
                .getBody();
    }
}
