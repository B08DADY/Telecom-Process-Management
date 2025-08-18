package com.Boghtech.MinAPI.dto;

import com.Boghtech.MinAPI.validation.EgyptianPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WorkOrderRequestDTO(
        @NotNull(message = "Customer Name is Required")
        String customerName,
        @Email(message = "Customer Email Must be Valid")
        String customerEmail,

        String customerAddress,
        @NotNull(message = "Customer Phone is Required")
        @EgyptianPhoneNumber
        String customerPhone,

        @NotNull(message = "WorkOrder Description Phone is Required")
        String workOrderDescription,

        @FutureOrPresent(message = "Visit date cannot be in the past.")
        LocalDate visitDate

) {
}
