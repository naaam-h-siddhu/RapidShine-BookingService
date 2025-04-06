package com.rapidshine.carwash.bookingservice.dto;

import com.rapidshine.carwash.bookingservice.model.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarListDto {
    private List<CarDto> cars;

}
