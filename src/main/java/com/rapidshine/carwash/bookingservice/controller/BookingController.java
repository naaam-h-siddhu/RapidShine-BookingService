package com.rapidshine.carwash.bookingservice.controller;

import com.rapidshine.carwash.bookingservice.dto.*;
import com.rapidshine.carwash.bookingservice.service.BookingService;
import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.WasherStatusPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private WasherStatusPublisher washerStatusPublisher;
    @PostMapping("/car/{id}")
    public BookingResponseDto bookCarWash(Authentication authentication,

                                          @PathVariable Long id) throws Exception {
        BookingResponseDto bookingResponseDto = bookingService.book(authentication.getName(), id);
        return bookingResponseDto;
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBooingDetails(Authentication authentication, @PathVariable Long id) throws Exception {
        return bookingService.getBookingDetail(authentication.getName(), id);
    }

    @GetMapping("/car")
    public CarListDto getCars(Authentication authentication) throws Exception {
        return bookingService.getAllCars(authentication.getName());
    }

    @GetMapping("/")
    public BookingListDto getAllBooking(Authentication authentication) throws Exception {
        return bookingService.getAllBookings(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public BookingResponseDto cancelBooking(Authentication authentication, @PathVariable Long id) throws Exception {
        return bookingService.cancelBooking(authentication.getName(), id);
    }

    //    @GetMapping("/assign")
    //    public String assign(){
    //        return bookingService.assignWasher();
    //    }

    @GetMapping("/get_payment_url/{id}")
    public BookingResponseDto getPaymentUrl(Authentication authentication,@PathVariable Long id) throws Exception{
        return bookingService.getPaymentUrl(authentication.getName(),id);
    }

    @GetMapping("/get_washer/{id}")
    public String getWasher(Authentication authentication,@PathVariable Long id) throws  Exception{
        return bookingService.getWasher(authentication.getName(),id);
    }
    @GetMapping("/dashboard/summary")
    public ResponseEntity<Map<String, Integer>> getCustomerBookingSummary(Authentication authentication) throws Exception {

        return bookingService.getBookingSummary(authentication.getName());
    }

    //internal endpoint for checking if the washer is working or not
    @GetMapping("/isWorking")
    public boolean isWorking(@RequestParam String email){
        System.out.println("BABY ZINDA HAI ......");

        return bookingService.checkIfWasherIsWorking( email);

    }
}
