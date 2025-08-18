package com.Boghtech.MinAPI.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AssignTechnicianRequestDTO(
        @NotNull(message = "Technician ID is required.")
                                          Long technicianId
,
        @NotNull(message = "Visit Date is required.")
        LocalDate visitDate) {
}
