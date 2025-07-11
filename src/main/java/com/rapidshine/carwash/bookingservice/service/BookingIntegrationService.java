    package com.rapidshine.carwash.bookingservice.service;

    import com.rapidshine.carwash.bookingservice.dto.CarDto;
    import com.rapidshine.carwash.bookingservice.dto.CarListDto;
    import com.rapidshine.carwash.bookingservice.dto.StripeRequest;
    import com.rapidshine.carwash.bookingservice.dto.StripeResponse;
    import com.rapidshine.carwash.bookingservice.feign.CarServiceClient;
    import com.rapidshine.carwash.bookingservice.feign.PaymentServiceClient;
    import com.rapidshine.carwash.bookingservice.feign.WasherServiceClient;
    import com.rapidshine.carwash.bookingservice.model.Washer;
    import lombok.RequiredArgsConstructor;
    import org.apache.commons.lang3.concurrent.CircuitBreaker;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
    import org.springframework.context.annotation.Fallback;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;

    @Service
    public class BookingIntegrationService {
        @Autowired
        private  CarServiceClient carServiceClient;
        @Autowired
        private  WasherServiceClient washerServiceClient;
        @Autowired
        private  PaymentServiceClient paymentServiceClient;
        @Autowired
        private  CircuitBreakerFactory<?,?> circuitBreakerFactory;

        public CarListDto getAllCars(Long customer_id){
            System.out.println("Booking is coming here");
           return circuitBreakerFactory.create("carServiceCB")
                    .run(()-> carServiceClient.getCars(customer_id), throwable -> {
                        System.out.println("🚨 Fallback triggered for getAllCars: " + throwable.getMessage());

                        return handleGetAllCarFallback();
                            }
                    );
        }
        public CarDto getCarById(Long id,Long customer_id){
            return circuitBreakerFactory.create("carServiceCB")
                    .run(()->carServiceClient.getCarById(id,customer_id), throwable -> handleGetCarByIdFallback(id)
                    );
        }


        public List<Washer> getListOfWasher(){
            return circuitBreakerFactory.create("washerServiceCB")
                    .run(washerServiceClient::getListOfWasher, throwable -> handleGetListOfWasherFallback());
        }



        public StripeResponse paymentCheckout(StripeRequest stripeRequest){
            return  circuitBreakerFactory.create("paymentServiceCB")
                    .run(()->paymentServiceClient.
                            paymentCheckout(stripeRequest),throwable -> handlePaymentCheckoutFallback());
        }

        // <--------- FALL BACK METHODS ------->
        private CarListDto handleGetAllCarFallback(){
            List<CarDto> carDtoList = new ArrayList<>();
            carDtoList.add(new CarDto(0L,"Unavailable","Unavailable","Unavailable","Unavailable"));
            return new CarListDto(carDtoList    );
        }

        private CarDto handleGetCarByIdFallback(Long id){
            return new CarDto(id,"Unavailable","Unavailable","Unavailable","Unavailable");
        }
        private List<Washer> handleGetListOfWasherFallback(){
            List<Washer> list = new ArrayList<>();
            list.add(new Washer("Unavailable","Unavailable","Unavailable","Unavailable",false));
            return  list;
        }
        private StripeResponse handlePaymentCheckoutFallback(){
            return new StripeResponse("0","unavailable");
        }
    }
