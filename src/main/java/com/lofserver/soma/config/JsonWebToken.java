package com.lofserver.soma.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.time.Duration;
import java.util.Date;

@Component
public class JsonWebToken {

    private final String secretKey;

    @Autowired
    public JsonWebToken(@Value("${JWT.signature}") String secretKey){
        this.secretKey = secretKey;
    }
    public String makeJwtTokenById(Long id) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // header type (jwt)
                .setIssuer("lof-server") // (iss setting)
                .setIssuedAt(now) // (iat setting)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // (exp setting)
                .claim("id", id) // (secret claim setting)
                .signWith(SignatureAlgorithm.HS256, secretKey) // (signature setting)
                .compact();
    }

    public Claims parseJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
