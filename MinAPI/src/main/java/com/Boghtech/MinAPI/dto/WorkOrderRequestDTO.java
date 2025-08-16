package com.Boghtech.MinAPI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record WorkOrderRequestDTO(
        @NotNull(message = "Customer Name is Required")
        String customerName,
        @NotNull(message = "Customer Email is Required")
        @Email(message = "Customer Email Must be Valid")
        String customerEmail,
        @NotNull(message="Customer Address is Required")
        String customerAddress,
        @NotNull(message = "Customer Phone is Required")
        String customerPhone,

        @NotNull(message = "WorkOrder Description Phone is Required")
        String workOrderDescription

) {
}
