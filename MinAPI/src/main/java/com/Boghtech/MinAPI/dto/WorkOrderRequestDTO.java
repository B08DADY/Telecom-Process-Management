package com.Boghtech.MinAPI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

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
        String workOrderDescription,

        @FutureOrPresent(message = "Visit date cannot be in the past.")
        LocalDate visitDate

) {
}
