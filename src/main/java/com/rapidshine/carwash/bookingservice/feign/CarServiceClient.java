package com.rapidshine.carwash.bookingservice.feign;

import com.rapidshine.carwash.bookingservice.config.FeignClientConfig;
import com.rapidshine.carwash.bookingservice.dto.CarDto;
import com.rapidshine.carwash.bookingservice.dto.CarListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "car-service",configuration = FeignClientConfig.class)
public interface CarServiceClient {

    @GetMapping("/car")
    CarListDto getCars();

    @GetMapping("/car/{id}")
    CarDto getCarById( @PathVariable("id") Long id);
}

