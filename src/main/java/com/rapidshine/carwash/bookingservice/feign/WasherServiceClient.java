package com.rapidshine.carwash.bookingservice.feign;

import com.rapidshine.carwash.bookingservice.config.FeignClientConfig;
import com.rapidshine.carwash.bookingservice.config.FeignInternalServiceTokenConfig;
import com.rapidshine.carwash.bookingservice.model.Washer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "washer-service",configuration = FeignInternalServiceTokenConfig.class)
public interface WasherServiceClient {
    @GetMapping("/washer/available")
    List<Washer> getAvailableWasher();
}
