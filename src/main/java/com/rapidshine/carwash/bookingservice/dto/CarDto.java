package com.rapidshine.carwash.bookingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long carId;
    private String brand;
    private String model;
    private String licenceNumberPlate;

}
