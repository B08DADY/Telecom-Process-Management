package com.Boghtech.MinAPI.controller;

import com.Boghtech.MinAPI.dto.TechnicianRequestDTO;
import com.Boghtech.MinAPI.dto.TechnicianResponseDTO;
import com.Boghtech.MinAPI.service.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping
    public ResponseEntity<TechnicianResponseDTO> createTechnician(@Valid @RequestBody TechnicianRequestDTO requestDTO) {
        TechnicianResponseDTO newTechnician = technicianService.createTechnician(requestDTO);
        return new ResponseEntity<>(newTechnician, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TechnicianResponseDTO>> getAllTechnicians() {
        List<TechnicianResponseDTO> technicians = technicianService.getAllTechnicians();
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianResponseDTO> getTechnicianById(@PathVariable Long id) {
        TechnicianResponseDTO technician = technicianService.getTechnicianById(id);
        return ResponseEntity.ok(technician);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnicianById(@PathVariable Long id) {
        technicianService.deleteTechnicianById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<TechnicianResponseDTO>> getAvailableTechnicians(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TechnicianResponseDTO> availableTechnicianIds = technicianService.findAvailableTechniciansByDate(date);
        return ResponseEntity.ok(availableTechnicianIds);
    }

}
