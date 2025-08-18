package com.Boghtech.MinAPI.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReAssignTechnicianRequestDTO(
        @NotNull(message = "Technician ID is required.")
                                          Long newTechnicianId ){
}
