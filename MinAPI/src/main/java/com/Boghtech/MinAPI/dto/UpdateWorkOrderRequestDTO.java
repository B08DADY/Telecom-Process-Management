package com.Boghtech.MinAPI.dto;

import com.Boghtech.MinAPI.validation.EgyptianPhoneNumber;
import jakarta.validation.constraints.Email;

public record UpdateWorkOrderRequestDTO(
        String workOrderDescription,
        String customerName,
        @Email(message = "If provided, email must be a valid format.")
        String customerEmail,
        @EgyptianPhoneNumber
        String customerPhone,
        String customerAddress
) {
}