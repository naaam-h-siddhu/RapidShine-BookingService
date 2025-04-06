package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.BookingRequestDto;
import com.rapidshine.carwash.bookingservice.dto.BookingResponseDto;
import com.rapidshine.carwash.bookingservice.dto.CarDto;
import com.rapidshine.carwash.bookingservice.dto.CarListDto;
import com.rapidshine.carwash.bookingservice.exceptions.UserNotFoundException;
import com.rapidshine.carwash.bookingservice.feign.CarServiceClient;
import com.rapidshine.carwash.bookingservice.model.Customer;
import com.rapidshine.carwash.bookingservice.model.User;
import com.rapidshine.carwash.bookingservice.repository.CustomerRepository;
import com.rapidshine.carwash.bookingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarServiceClient carClient;
    public BookingResponseDto book(String name, BookingRequestDto bookingRequestDto) throws Exception {

        return null;

    }
    public CarListDto getAllCars(){
        return carClient.getCars();
    }


    // helper function to get the customer using the email
    private Customer getCustomer(String email) throws Exception {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with "+email+" not " +
                "found"));
        Long id = user.getId();
        Customer customer = customerRepository.findByUserId(id).orElseThrow(() -> new UserNotFoundException("Customer " +
                "with " + email + " not found"));
        return customer;

    }


}
