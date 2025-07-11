package com.rapidshine.carwash.bookingservice.config;

import com.rapidshine.carwash.bookingservice.service.TokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig  {

    @Bean
    public RequestInterceptor requestInterceptor(TokenManager tokenManager){
        return new FeignClientInterceptor(tokenManager);
    }

//    @Override
//    public void apply(RequestTemplate template) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//
//        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
//            HttpServletRequest request = servletRequestAttributes.getRequest();
//            String authHeader = request.getHeader("Authorization");
//
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                template.header("Authorization", authHeader);
//            }
//        }
//    }
}
