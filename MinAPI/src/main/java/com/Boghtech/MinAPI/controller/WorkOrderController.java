package com.Boghtech.MinAPI.controller;

import com.Boghtech.MinAPI.dto.WorkOrderRequestDTO;
import com.Boghtech.MinAPI.dto.WorkOrderResponseDTO;
import com.Boghtech.MinAPI.model.WorkOrder;
import com.Boghtech.MinAPI.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workorders")
public class WorkOrderController {

    private WorkOrderService workOrderService;
    @Autowired
    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponseDTO> addWorkOrder(@RequestBody WorkOrderRequestDTO workOrderRequest) {
        WorkOrderResponseDTO workOrderResponse=workOrderService.save(workOrderRequest);
        return ResponseEntity.ok(workOrderResponse);
    }
}
