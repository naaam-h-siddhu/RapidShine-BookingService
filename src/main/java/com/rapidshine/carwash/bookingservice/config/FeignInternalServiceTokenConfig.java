package com.rapidshine.carwash.bookingservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class FeignInternalServiceTokenConfig implements RequestInterceptor {
    @Value("${service.jwt.token}")
    private String serviceToken;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + serviceToken);
    }
}
