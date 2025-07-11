package com.rapidshine.carwash.bookingservice.feign;

import com.rapidshine.carwash.bookingservice.config.FeignClientConfig;
import com.rapidshine.carwash.bookingservice.dto.StripeRequest;
import com.rapidshine.carwash.bookingservice.dto.StripeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name= "payment-service" , configuration = FeignClientConfig.class)
public interface PaymentServiceClient {

    @GetMapping("/payments/checkout")
    StripeResponse paymentCheckout(@RequestBody StripeRequest stripeRequest);

}
