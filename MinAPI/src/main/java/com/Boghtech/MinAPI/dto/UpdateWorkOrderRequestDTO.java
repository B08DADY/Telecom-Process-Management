package com.Boghtech.MinAPI.dto;

import jakarta.validation.constraints.Email;

public record UpdateWorkOrderRequestDTO(
        String workOrderDescription,
        String customerName,
        @Email(message = "If provided, email must be a valid format.")
        String customerEmail,
        String customerPhone,
        String customerAddress
) {
}