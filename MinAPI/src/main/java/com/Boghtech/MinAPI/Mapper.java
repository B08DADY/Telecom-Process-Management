package com.Boghtech.MinAPI;

import com.Boghtech.MinAPI.dto.TechnicianRequestDTO;
import com.Boghtech.MinAPI.dto.TechnicianResponseDTO;
import com.Boghtech.MinAPI.dto.WorkOrderRequestDTO;
import com.Boghtech.MinAPI.dto.WorkOrderResponseDTO;
import com.Boghtech.MinAPI.model.Customer;
import com.Boghtech.MinAPI.model.Technician;
import com.Boghtech.MinAPI.model.WorkOrder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class Mapper {
    public static Customer toCustomer(WorkOrderRequestDTO workOrderRequestDTO) {
        Customer customer = new Customer();
        customer.setName(workOrderRequestDTO.customerName());
        customer.setPhone(workOrderRequestDTO.customerPhone());
        if (workOrderRequestDTO.customerEmail() != null) {
            customer.setEmail(workOrderRequestDTO.customerEmail());
        }
        if (workOrderRequestDTO.customerAddress() != null) {
            customer.setAddress(workOrderRequestDTO.customerAddress());
        }
        return customer;
    }
    public static WorkOrder toWorkOrder(WorkOrderRequestDTO workOrderRequestDTO, Customer customer) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setCustomer(customer);
        workOrder.setDescription(workOrderRequestDTO.workOrderDescription());
        workOrder.setCreatedAt(LocalDate.now());

        if (workOrderRequestDTO.visitDate() != null) {
            workOrder.setVisitDate(workOrderRequestDTO.visitDate());
        }

        return workOrder;
    }
    public static WorkOrderResponseDTO toWorkOrderResponse(WorkOrder workOrder, Customer customer) {
        if(workOrder.getTechnician()!=null)
           return new WorkOrderResponseDTO(Long.toString(workOrder.getId()),workOrder.getDescription()
            ,Long.toString(customer.getId()),customer.getName(),customer.getEmail(),customer.getPhone(),customer.getAddress(),workOrder.getCreatedAt().toString(),workOrder.getWorkOrderStatues(), Long.toString(workOrder.getTechnician().getId()));
        else
            return new WorkOrderResponseDTO(Long.toString(workOrder.getId()),workOrder.getDescription()
                    ,Long.toString(customer.getId()),customer.getName(),customer.getEmail(),
                    customer.getPhone(),customer.getAddress(),workOrder.getCreatedAt().toString(),
                    workOrder.getWorkOrderStatues(), null);

    }
    public static Technician toTechnician(TechnicianRequestDTO dto) {
        Technician technician = new Technician();
        technician.setName(dto.name());
        technician.setEmail(dto.email());
        technician.setPhone(dto.phone());
        technician.setAddress(dto.address());
        return technician;
    }

    public static TechnicianResponseDTO toTechnicianResponse(Technician technician) {
        return new TechnicianResponseDTO(
                Long.toString(technician.getId()),
                technician.getName(),
                technician.getEmail(),
                technician.getPhone(),
                technician.getAddress()
        );
    }

}
