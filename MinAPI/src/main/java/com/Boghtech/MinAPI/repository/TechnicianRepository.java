package com.Boghtech.MinAPI.repository;

import com.Boghtech.MinAPI.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    @Query("SELECT t.id " +
            "FROM Technician t " +
            "LEFT JOIN TechSlot ts ON t.id = ts.technician.id AND ts.visitDate = :visitDate " +
            "GROUP BY t.id " +
            "HAVING COUNT(ts.id) < :totalSlots")
    List<Long> findTechnicianIdsWithAvailableSlots(
            @Param("visitDate") LocalDate visitDate,
            @Param("totalSlots") long totalSlots
    );
}
