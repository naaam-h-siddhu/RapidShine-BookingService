package com.rapidshine.carwash.bookingservice.config;

import com.rapidshine.carwash.bookingservice.service.TokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    private final TokenManager tokenManager;
    @Override
    public void apply(RequestTemplate template){
        String m2mToken = tokenManager.getM2MToken();
        template.header("Authorization","Bearer "+m2mToken);
    }
}
