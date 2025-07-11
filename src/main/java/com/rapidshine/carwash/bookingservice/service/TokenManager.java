package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.TokenResponse;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenManager {
    private final RestTemplate restTemplate;
    private final Environment env;

    public String getM2MToken(){
        String tokenUrl = env.getProperty("auth.m2m.token-url");
        String clientId = env.getProperty("auth.m2m.client-id");
        String clientSecret = env.getProperty("auth.m2m.client-secret");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String ,String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("client_id",clientId);
        body.put("client_secret",clientSecret);
        HttpEntity<Map<String,String>> request = new HttpEntity<>(body,headers);
        try{
            ResponseEntity<TokenResponse> response = restTemplate.exchange(tokenUrl, HttpMethod.POST,request, TokenResponse.class);
            return response.getBody().getAccess_token();
        }catch (Exception e){
            log.error("Failed to fetch M2M token : {}",e.getMessage());
            throw  new RuntimeException("Unable to get M2M Token");
        }
    }

}
