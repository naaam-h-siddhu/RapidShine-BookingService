package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.*;
import com.rapidshine.carwash.bookingservice.exceptions.UserNotFoundException;
import com.rapidshine.carwash.bookingservice.feign.CarServiceClient;
import com.rapidshine.carwash.bookingservice.feign.WasherServiceClient;
import com.rapidshine.carwash.bookingservice.model.*;
import com.rapidshine.carwash.bookingservice.repository.BookingRepository;
import com.rapidshine.carwash.bookingservice.repository.CustomerRepository;
import com.rapidshine.carwash.bookingservice.repository.PaymentRepository;
import com.rapidshine.carwash.bookingservice.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private WasherServiceClient washerServiceClient;
    @Autowired
    private WasherStatusPublisher washerStatusPublisher;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public BookingResponseDto book(String email,BookingRequestDto bookingRequestDto,Long id) throws Exception {

        Customer customer = getCustomer(email);
        CarDto car = carClient.getCarById(id);

        Booking booking = new Booking();
        booking.setBookingStatus(bookingRequestDto.getBookingStatus());
        booking.setBookingTime(LocalDateTime.now());
        booking.setCustomer(customer);
        booking.setCar(new Car(car.getCarId(), car.getModel(), car.getModel(), car.getLicenceNumberPlate()));
        booking.setBookingStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(PaymentMethod.CASH);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setBooking(booking);
        paymentRepository.save(payment);
        //TODO Implement the payment service and use rabbitMQ/ Client to use that service

        bookingRepository.save(booking);
        return getBookingResponseDto(booking);
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
        return getBookingResponseDto(booking);

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
                    new PaymentResponseDto(booking.getPayment().getPaymentId(),
                            booking.getPayment().getPaymentMethod(),booking.getPayment().getPaymentStatus())
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
        if(booking.getBookingStatus() == BookingStatus.CONFIRMED) {

            washerStatusPublisher.updateWashwerStatus(booking.getWasherEmail(), true);
        }
        bookingRepository.save(booking);
        return getBookingResponseDto(booking);

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



    //Todo = testing it now , make it scheduled after testing
//    @Scheduled(fixedRate = 30000)
    public String assignWasher(){
        StringBuilder stringBuilder = new StringBuilder();
        List<Booking> unassignedBookings = bookingRepository.findByBookingStatus(BookingStatus.PENDING);

        if(unassignedBookings.isEmpty()){
            return stringBuilder.append("No booking left for assignment").toString();


        }
        List<Washer> washers = washerServiceClient.getAvailableWasher();
        if(washers.isEmpty()){
            return stringBuilder.append("No washer left for assignment").toString();

        }
        Random random = new Random();
        for(Booking booking1 : unassignedBookings){

            Washer washer = washers.get(random.nextInt(washers.size()));
            booking1.setWasherEmail(washer.getEmail());
            booking1.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking1);
            washerStatusPublisher.updateWashwerStatus(washer.getEmail(),false);
            stringBuilder.append(booking1.getBookingId()+washer.getEmail()+"\n");

        }
        return stringBuilder.toString();


    }
    private BookingResponseDto getBookingResponseDto(Booking booking){
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBookingId(booking.getBookingId());
        bookingResponseDto.setBookingTime(booking.getBookingTime());
        bookingResponseDto.setBookingStatus(booking.getBookingStatus());
        bookingResponseDto.setCarDto(carClient.getCarById(booking.getCar().getCarId()));
        bookingResponseDto.setPayment(new PaymentResponseDto(100L, PaymentMethod.CASH, PaymentStatus.PAID));
        return bookingResponseDto;
    }



}
