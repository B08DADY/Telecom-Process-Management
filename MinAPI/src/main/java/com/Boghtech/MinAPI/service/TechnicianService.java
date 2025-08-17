package com.Boghtech.MinAPI.service;

import com.Boghtech.MinAPI.Mapper;
import com.Boghtech.MinAPI.dto.TechnicianRequestDTO;
import com.Boghtech.MinAPI.dto.TechnicianResponseDTO;
import com.Boghtech.MinAPI.exception.ResourceNotFoundException;
import com.Boghtech.MinAPI.model.Technician;
import com.Boghtech.MinAPI.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {
    private final TechnicianRepository technicianRepository;
    private static final long TOTAL_POSSIBLE_SLOTS = 7L;
    @Autowired
    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    public TechnicianResponseDTO createTechnician(TechnicianRequestDTO requestDTO) {
        Technician technician = Mapper.toTechnician(requestDTO);
        Technician savedTechnician = technicianRepository.save(technician);
        return Mapper.toTechnicianResponse(savedTechnician);
    }

    public List<TechnicianResponseDTO> getAllTechnicians() {
        return technicianRepository.findAll()
                .stream()
                .map(Mapper::toTechnicianResponse)
                .collect(Collectors.toList());
    }

    public TechnicianResponseDTO getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .map(Mapper::toTechnicianResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Technician with ID " + id + " not found."));
    }

    public void deleteTechnicianById(Long id) {
        if (!technicianRepository.existsById(id)) {
            throw new ResourceNotFoundException("Technician with ID " + id + " not found.");
        }
        technicianRepository.deleteById(id);
    }
    public List<Long> findAvailableTechniciansByDate(LocalDate date) {
        return technicianRepository.findTechnicianIdsWithAvailableSlots(date, TOTAL_POSSIBLE_SLOTS);
    }
}
