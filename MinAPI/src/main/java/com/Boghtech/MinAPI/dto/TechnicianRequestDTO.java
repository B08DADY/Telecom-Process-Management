package com.Boghtech.MinAPI.dto;

import com.Boghtech.MinAPI.validation.EgyptianPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TechnicianRequestDTO(
        @NotNull(message = "Name is required.")
        String name,

        @NotNull(message = "Email is required.")
        @Email(message = "Email must be a well-formed email address.")
        String email,

        @NotNull(message = "Phone number is required.")
        @EgyptianPhoneNumber
        String phone,

        @NotNull(message = "Address is required.")
        String address
) {
}
