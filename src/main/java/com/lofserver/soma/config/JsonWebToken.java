package com.lofserver.soma.config;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
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
                .setExpiration(new Date(now.getTime() + Duration.ofDays(1).toMillis())) // (exp setting)
                .claim("id", id) // (secret claim setting)
                .signWith(SignatureAlgorithm.HS256, secretKey) // (signature setting)
                .compact();
    }

    public Claims parseJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean checkJwtToken(String token){
        try{
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
        }catch (ExpiredJwtException e){
            log.info("ExpiredJwtException");
            return false;
        }catch (Exception e){
            log.info("JWTException");
            return false;
        }
        return true;
    }
}
