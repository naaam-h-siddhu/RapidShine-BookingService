package com.rapidshine.carwash.bookingservice.service;

import com.rapidshine.carwash.bookingservice.dto.*;
import com.rapidshine.carwash.bookingservice.exceptions.CarUnavailableException;
import com.rapidshine.carwash.bookingservice.exceptions.PaymentUnavailableException;
import com.rapidshine.carwash.bookingservice.exceptions.UserNotFoundException;
import com.rapidshine.carwash.bookingservice.feign.CarServiceClient;
import com.rapidshine.carwash.bookingservice.feign.WasherServiceClient;
import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.BookingNotificationPublisher;
import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.NotificationUpdatePublisher;
import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.WasherServiceCountPublisher;
import com.rapidshine.carwash.bookingservice.messaging.rabbitmq.WasherStatusPublisher;
import com.rapidshine.carwash.bookingservice.model.*;
import com.rapidshine.carwash.bookingservice.repository.BookingRepository;
import com.rapidshine.carwash.bookingservice.repository.CustomerRepository;
import com.rapidshine.carwash.bookingservice.repository.PaymentRepository;
import com.rapidshine.carwash.bookingservice.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WasherServiceClient washerServiceClient;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WasherStatusPublisher washerStatusPublisher;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BookingIntegrationService bookingIntegrationService;

    @Autowired
    private WasherServiceCountPublisher washerServiceCountPublisher;

    @Autowired
    private BookingNotificationPublisher bookingNotificationPublisher;

    @Autowired
    private CarServiceClient carServiceClient;

    @Autowired
    private NotificationUpdatePublisher notificationUpdatePublisher;
    /**
     * Books a car wash by creating a booking, payment, and assigning a Stripe payment URL.
     */
    public BookingResponseDto book(String email, Long id) throws Exception {
        Customer customer = getCustomer(email);
        CarDto car = bookingIntegrationService.getCarById(id, customer.getCustomerID());

        if (car.getBrand().equalsIgnoreCase("unavailable")) {
            throw new CarUnavailableException("Car is currently unavailable for booking");
        }

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingTime(LocalDateTime.now());
        booking.setCustomer(customer);
        booking.setCar(new Car(car.getCarId(), car.getBrand(), car.getCarType(), car.getLicenceNumberPlate()));

        bookingRepository.save(booking);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(PaymentMethod.CASH);
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        payment.setBooking(booking);

        paymentRepository.save(payment);

        StripeResponse stripeResponse = bookingIntegrationService.paymentCheckout(
                new StripeRequest(1L, 1L, payment.getPaymentId().toString(), "IND")
        );

        if (stripeResponse.getUrl().equalsIgnoreCase("unavailable")) {
            throw new PaymentUnavailableException("Payment service not working");
        }

        bookingNotificationPublisher.bookingNotificationStatus(booking.getBookingId(), email);
        customer.setTotal_bookings(customer.getTotal_bookings() + 1);
        customerRepository.save(customer);
        bookingRepository.save(booking);

        washerServiceCountPublisher.updateWashwerStatus(booking.getWasherEmail(), true);

        return getBookingResponseDto(booking, stripeResponse.getUrl(), car);
    }

    /**
     * Retrieves all cars owned by a customer using their email.
     */
    public CarListDto getAllCars(String email) throws Exception {
        Customer customer = getCustomer(email);
        return bookingIntegrationService.getAllCars(customer.getCustomerID());
    }

    /**
     * Returns the details of a specific booking after verifying ownership.
     */
    public BookingResponseDto getBookingDetail(String email, Long id) throws Exception {
        Customer customer = getCustomer(email);
        Booking booking = bookingRepository.getBookingsByBookingId(id);

        if (!Objects.equals(customer.getCustomerID(), booking.getCustomer().getCustomerID())) {
            throw new RuntimeException("You do not have any access to this booking ");
        }

        return getBookingResponseDto(booking, "", bookingIntegrationService.getCarById(booking.getCar().getCarId(), customer.getCustomerID()));
    }

    /**
     * Returns all bookings made by a customer along with related car and payment details.
     */
    public BookingListDto getAllBookings(String email) throws Exception {
        Customer customer = getCustomer(email);
        List<Booking> bookings = bookingRepository.findByCustomerId(customer.getCustomerID());

        List<BookingResponseDto> responses = bookings.stream().map(booking -> {
            CarDto carDto = new CarDto(
                    booking.getCar().getCarId(),
                    booking.getCar().getBrand(),
                    booking.getCar().getModel(),
                    booking.getCar().getLicenceNumberPlate(),
                    booking.getCar().getCarType()
            );

            return new BookingResponseDto(
                    booking.getBookingId(),
                    booking.getBookingTime(),
                    booking.getBookingStatus(),
                    carDto,
                    new PaymentResponseDto(booking.getPayment().getPaymentId(), booking.getPayment().getPaymentMethod(), booking.getPayment().getPaymentStatus())
            );
        }).collect(Collectors.toList());

        return new BookingListDto(responses);
    }

    /**
     * Cancels a booking after verifying that the user is the owner.
     */
    public BookingResponseDto cancelBooking(String email, Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception("Booking not found with ID: " + id));

        if (!booking.getCustomer().getUser().getEmail().equals(email)) {
            throw new Exception("Unauthorized to cancel this booking.");
        }

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            washerStatusPublisher.updateWashwerStatus(booking.getWasherEmail(), true);
            notificationUpdatePublisher.addNotification(booking.getWasherEmail(),"The service for booking id "+booking.getBookingId()+" has been cancelled by customer",UserRole.WASHER);
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return getBookingResponseDto(booking, "", bookingIntegrationService.getCarById(booking.getCar().getCarId(), getCustomer(email).getCustomerID()));
    }

    /**
     * Helper method to retrieve a Customer by their email.
     */
    private Customer getCustomer(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with " + email + " not found"));
        Long id = user.getId();

        return customerRepository.findByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("Customer with " + email + " not found"));
    }

    /**
     * Assigns a washer to all unassigned bookings in PAYMENT_DONE state. Runs every 30 seconds.
     */


    /**
     * Builds and returns a BookingResponseDto using booking, link, and car details.
     */
    private BookingResponseDto getBookingResponseDto(Booking booking, String lnk, CarDto carDto) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBookingId(booking.getBookingId());
        bookingResponseDto.setBookingTime(booking.getBookingTime());
        bookingResponseDto.setBookingStatus(booking.getBookingStatus());
        bookingResponseDto.setCarDto(carDto);
        bookingResponseDto.setPayment(new PaymentResponseDto(100L, PaymentMethod.CASH, PaymentStatus.UNPAID));
        bookingResponseDto.setPaymentLink(lnk);
        return bookingResponseDto;
    }

    /**
     * Retrieves the Stripe payment URL for a booking, depending on its current status.
     */
    public BookingResponseDto getPaymentUrl(String email, Long id) throws Exception {
        Customer customer = getCustomer(email);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Booking not found"));

        CarDto carDto = new CarDto(
                booking.getCar().getCarId(),
                booking.getCar().getBrand(),
                booking.getCar().getModel(),
                booking.getCar().getLicenceNumberPlate(),
                booking.getCar().getCarType()
        );

        if (booking.getBookingStatus() == BookingStatus.PENDING) {
            StripeResponse stripeResponse = bookingIntegrationService.paymentCheckout(
                    new StripeRequest(1L, 1L, booking.getPayment().getPaymentId().toString(), "IND")
            );
            return getBookingResponseDto(booking, stripeResponse.getUrl(), carDto);
        } else if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            return getBookingResponseDto(booking, "Booking is cancelled", carDto);
        } else {
            return getBookingResponseDto(booking, "Payment already done", carDto);
        }
    }

    /**
     * Returns washer email assigned to a customer's specific booking.
     */
    public String getWasher(String name, Long id) throws Exception {
        Customer customer = getCustomer(name);
        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED || booking.getBookingStatus() == BookingStatus.COMPLETED) {
            return booking.getWasherEmail();
        }
        return null;
    }

    /**
     * Provides a summary of total, pending, and completed bookings for a customer.
     */
    public ResponseEntity<Map<String, Integer>> getBookingSummary(String email) throws Exception {
        Customer customer = getCustomer(email);
        int total = bookingRepository.countByCustomerId(customer.getCustomerID());
        int pending = bookingRepository.countByCustomerIdAndBookingStatus(customer.getCustomerID(), BookingStatus.PENDING);
        int completed = bookingRepository.countByCustomerIdAndBookingStatus(customer.getCustomerID(), BookingStatus.COMPLETED);

        Map<String, Integer> summary = new HashMap<>();
        summary.put("total", total);
        summary.put("pending", pending);
        summary.put("completed", completed);

        return ResponseEntity.ok(summary);
    }

    /**
     * Checks if a washer is currently working on any confirmed booking.
     */
    public boolean checkIfWasherIsWorking(String email) {
        System.out.println("Im here");
        Booking booking = bookingRepository.findByBookingByWasherEmail(email).orElse(null);
        return booking != null;
    }
}
