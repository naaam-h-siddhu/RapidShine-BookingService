package com.rapidshine.carwash.bookingservice.controller;

import com.rapidshine.carwash.bookingservice.dto.*;
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
                                          @RequestBody BookingRequestDto bookingRequestDto,
                                          @PathVariable Long id) throws Exception{
        BookingResponseDto bookingResponseDto= bookingService.book(authentication.getName(),bookingRequestDto,id);
        return bookingResponseDto;
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBooingDetails(Authentication authentication,@PathVariable Long id) throws Exception {
        return bookingService.getBookingDetail(authentication.getName(),id);
    }
    @GetMapping("/car")
    public CarListDto getCars() {
        return bookingService.getAllCars();
    }
    @GetMapping("/")
    public BookingListDto getAllBooking(Authentication authentication)throws Exception{
        return bookingService.getAllBookings(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public BookingResponseDto cancelBooking(Authentication authentication,@PathVariable Long id)throws Exception{
        return bookingService.cancelBooking(authentication.getName(),id);
    }


}
