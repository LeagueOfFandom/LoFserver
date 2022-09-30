package com.lofserver.soma.service.api.user;

import com.lofserver.soma.dto.google.GoogleUserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GoogleAccessService {
    public GoogleUserInfoDto getUserInfo(String accessToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + accessToken;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<GoogleUserInfoDto> googleUserInfoDto = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GoogleUserInfoDto.class);
            return googleUserInfoDto.getBody();
        }catch (Exception e){
            log.info("googleLogin : {}",e);
            return null;
        }
    }
}
