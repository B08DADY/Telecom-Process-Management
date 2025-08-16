package com.Boghtech.MinAPI;

import com.Boghtech.MinAPI.dto.WorkOrderRequestDTO;
import com.Boghtech.MinAPI.dto.WorkOrderResponseDTO;
import com.Boghtech.MinAPI.model.Customer;
import com.Boghtech.MinAPI.model.WorkOrder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class Mapper {
    public static Customer toCustomer(WorkOrderRequestDTO workOrderRequestDTO) {
        Customer customer = new Customer();
        customer.setAddress(workOrderRequestDTO.customerName());
        customer.setEmail(workOrderRequestDTO.customerEmail());
        customer.setPhone(workOrderRequestDTO.customerPhone());
        customer.setName(workOrderRequestDTO.customerName());
        return customer;
    }
    public static WorkOrder toWorkOrder(WorkOrderRequestDTO workOrderRequestDTO,Customer customer) {
        WorkOrder workOrder= new WorkOrder();
        workOrder.setCustomer(customer);
        workOrder.setDescription(workOrderRequestDTO.workOrderDescription());
        workOrder.setCreatedAt(LocalDate.now());
        workOrder.setWorkOrderStatues("IN_PROGRESS");
        return workOrder;
    }
    public static WorkOrderResponseDTO toWorkOrderResponse(WorkOrder workOrder, Customer customer) {
        WorkOrderResponseDTO workOrderResponseDTO= new WorkOrderResponseDTO(Long.toString(workOrder.getId()),workOrder.getDescription()
        ,Long.toString(customer.getId()),customer.getName(),customer.getEmail(),customer.getPhone(),customer.getAddress(),workOrder.getCreatedAt().toString());

        return workOrderResponseDTO;
    }

}
