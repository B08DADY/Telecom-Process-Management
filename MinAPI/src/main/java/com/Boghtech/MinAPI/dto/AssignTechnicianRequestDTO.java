package com.Boghtech.MinAPI.dto;

import jakarta.validation.constraints.NotNull;

public record AssignTechnicianRequestDTO(
        @NotNull(message = "Technician ID is required.")
                                          Long technicianId) {
}
