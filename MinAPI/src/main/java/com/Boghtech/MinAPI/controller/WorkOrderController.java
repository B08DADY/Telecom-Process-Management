package com.Boghtech.MinAPI.controller;

import com.Boghtech.MinAPI.dto.*;
import com.Boghtech.MinAPI.model.WorkOrder;
import com.Boghtech.MinAPI.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workorders")
public class WorkOrderController {

    private WorkOrderService workOrderService;
    @Autowired
    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponseDTO> addWorkOrder(@Valid @RequestBody WorkOrderRequestDTO workOrderRequest) {
        WorkOrderResponseDTO workOrderResponse=workOrderService.save(workOrderRequest);
        return ResponseEntity.ok(workOrderResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long id) {
        workOrderService.closeWorkOrder(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<WorkOrderResponseDTO>> getAllWorkOrders() {
        List<WorkOrderResponseDTO> workOrders = workOrderService.findAll();
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<WorkOrderResponseDTO>> getUnassignedWorkOrders() {
        List<WorkOrderResponseDTO> unassignedWorkOrders = workOrderService.findUnassigned();
        return ResponseEntity.ok(unassignedWorkOrders);
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<WorkOrderResponseDTO> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianRequestDTO assignRequest) {

        WorkOrderResponseDTO assignedWorkOrder = workOrderService.assignTechnicianAndCreateSlot(id, assignRequest);
        return ResponseEntity.ok(assignedWorkOrder);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDTO> updateWorkOrder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWorkOrderRequestDTO requestDTO) {

        WorkOrderResponseDTO updatedWorkOrder = workOrderService.updateWorkOrder(id, requestDTO);
        return ResponseEntity.ok(updatedWorkOrder);
    }
    @PatchMapping("/{workOrderId}/reassign")
    public ResponseEntity<WorkOrderResponseDTO> reassignTechnician(
            @PathVariable Long workOrderId,
            @Valid @RequestBody ReAssignTechnicianRequestDTO request) {

        WorkOrderResponseDTO updatedWorkOrder = workOrderService.reassignTechnician(workOrderId, request);
        return ResponseEntity.ok(updatedWorkOrder);
    }

}
