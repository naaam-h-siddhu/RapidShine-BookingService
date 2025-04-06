package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.*;
import com.rapidshine.carwash.bookingservice.exceptions.UserNotFoundException;
import com.rapidshine.carwash.bookingservice.feign.CarServiceClient;
import com.rapidshine.carwash.bookingservice.model.*;
import com.rapidshine.carwash.bookingservice.repository.BookingRepository;
import com.rapidshine.carwash.bookingservice.repository.CustomerRepository;
import com.rapidshine.carwash.bookingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CarServiceClient carClient;
    public BookingResponseDto book(String email,BookingRequestDto bookingRequestDto,Long id) throws Exception {

        Customer customer = getCustomer(email);
        CarDto car = carClient.getCarById(id);

        Booking booking = new Booking();
        booking.setBookingStatus(bookingRequestDto.getBookingStatus());
        booking.setBookingTime(LocalDateTime.now());
        booking.setCustomer(customer);
        booking.setCar(new Car(car.getCarId(), car.getModel(), car.getModel(), car.getLicenceNumberPlate()));
        booking = bookingRepository.save(booking);
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(PaymentMethod.CASH);
        payment.setPaymentStatus(PaymentStatus.PAID);
        if(payment.getPaymentStatus() == PaymentStatus.PAID){
            booking.setBookingStatus(BookingStatus.CONFIRMED);
        }
        else{
            booking.setBookingStatus(BookingStatus.CANCELLED);
        }
        bookingRepository.save(booking);
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBookingId(booking.getBookingId());
        bookingResponseDto.setBookingTime(booking.getBookingTime());
        bookingResponseDto.setBookingStatus(booking.getBookingStatus());
        bookingResponseDto.setCarDto(new CarDto(car.getCarId(),car.getBrand(),car.getModel(),car.getLicenceNumberPlate()));
        bookingResponseDto.setPayment(payment);
        return bookingResponseDto;
    }
    public CarListDto getAllCars(){
        return carClient.getCars();
    }

    public BookingResponseDto getBookingDetail(String email,Long id)throws Exception {
        Customer customer = getCustomer(email);
        Booking booking = bookingRepository.getBookingsByBookingId(id);
        if (customer.getCustomerID() != booking.getCustomer().getCustomerID()) {
            throw new RuntimeException("You do not have any access to this booking ");
        }
        CarDto car = carClient.getCarById(booking.getCar().getCarId());
        return new BookingResponseDto(booking.getBookingId(), booking.getBookingTime(),booking.getBookingStatus(),
                car,
                booking.getPayment() );

    }


    public BookingListDto getAllBookings(String email) throws Exception {
        Customer customer = getCustomer(email);
        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerID());

        List<BookingResponseDto> responses = bookings.stream().map(booking -> {
            CarDto carDto = new CarDto(
                    booking.getCar().getCarId(),
                    booking.getCar().getBrand(),
                    booking.getCar().getModel(),
                    booking.getCar().getLicenceNumberPlate()
            );
            return new BookingResponseDto(
                    booking.getBookingId(),
                    booking.getBookingTime(),
                    booking.getBookingStatus(),
                    carDto,
                    booking.getPayment()
            );
        }).collect(Collectors.toList());

        return new BookingListDto(responses);
    }

    public BookingResponseDto cancelBooking(String email,Long id) throws Exception{
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking not found with ID: " + id));

        // Validate ownership
        if (!booking.getCustomer().getUser().getEmail().equals(email)) {
            throw new Exception("Unauthorized to cancel this booking.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return new BookingResponseDto(booking.getBookingId(), booking.getBookingTime(),booking.getBookingStatus(),
                new CarDto(booking.getCar().getCarId(),booking.getCar().getBrand(),booking.getCar().getModel(),
                        booking.getCar().getLicenceNumberPlate()),
                booking.getPayment() );

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
