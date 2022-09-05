package com.lofserver.soma.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class JsonWebTokenTest {

    @MockBean
    JsonWebToken jsonWebToken = new JsonWebToken("Test");

    @Test
    void makeJwtTokenById() {

        assertEquals(jsonWebToken.makeJwtTokenById(1L),jsonWebToken.makeJwtTokenById(1L));
    }

    @Value("${JWT.signature}")
    String test;
    @Test
    void parseJwtToken() {
        System.out.println(test);
        String token = jsonWebToken.makeJwtTokenById(1L);
        assertEquals(jsonWebToken.parseJwtToken(token).get("id"), 1);
    }
}