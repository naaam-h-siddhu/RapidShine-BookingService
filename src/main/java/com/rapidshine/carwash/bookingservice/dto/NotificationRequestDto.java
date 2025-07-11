package com.rapidshine.carwash.bookingservice.dto;

import com.rapidshine.carwash.bookingservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private String email;
    private String message;
    private UserRole role;
}
