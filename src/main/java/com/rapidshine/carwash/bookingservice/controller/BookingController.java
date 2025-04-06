package com.rapidshine.carwash.bookingservice.controller;

import com.rapidshine.carwash.bookingservice.dto.BookingRequestDto;
import com.rapidshine.carwash.bookingservice.dto.BookingResponseDto;
import com.rapidshine.carwash.bookingservice.dto.CarDto;
import com.rapidshine.carwash.bookingservice.dto.CarListDto;
import com.rapidshine.carwash.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @PostMapping("/car/{id}")
    public BookingResponseDto bookCarWash(Authentication authentication,
                                          @RequestBody BookingRequestDto bookingRequestDto) throws Exception{
        BookingResponseDto bookingResponseDto= bookingService.book(authentication.getName(),bookingRequestDto);
        return bookingResponseDto;
    }

    @GetMapping("/car")
    public CarListDto getCars() {
        return bookingService.getAllCars();
    }

}
