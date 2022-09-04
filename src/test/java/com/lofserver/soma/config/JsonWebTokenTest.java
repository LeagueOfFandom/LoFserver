package com.lofserver.soma.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonWebTokenTest {

    @Test
    void makeJwtTokenById() {
        JsonWebToken jsonWebToken = new JsonWebToken();
        assertEquals(jsonWebToken.makeJwtTokenById(1L),jsonWebToken.makeJwtTokenById(1L));
    }

    @Test
    void parseJwtToken() {
        JsonWebToken jsonWebToken = new JsonWebToken();
        String token = jsonWebToken.makeJwtTokenById(1L);
        assertEquals(jsonWebToken.parseJwtToken(token).get("id"), 1);
    }
}