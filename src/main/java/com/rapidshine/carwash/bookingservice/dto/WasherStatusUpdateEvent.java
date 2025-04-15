package com.rapidshine.carwash.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasherStatusUpdateEvent
{

    private String email;
    private boolean isAvailable;
}
