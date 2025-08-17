package com.Boghtech.MinAPI.repository;

import com.Boghtech.MinAPI.model.TechSlot;
import com.Boghtech.MinAPI.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechSlotRepository extends JpaRepository<TechSlot, Long> {
    Optional<TechSlot> findByWorkOrder(WorkOrder workOrder);
}
