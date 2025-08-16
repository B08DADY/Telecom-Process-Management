package com.Boghtech.MinAPI.service;

import com.Boghtech.MinAPI.Mapper;
import com.Boghtech.MinAPI.dto.WorkOrderRequestDTO;
import com.Boghtech.MinAPI.dto.WorkOrderResponseDTO;
import com.Boghtech.MinAPI.model.Customer;
import com.Boghtech.MinAPI.model.WorkOrder;
import com.Boghtech.MinAPI.repository.CustomerRepository;
import com.Boghtech.MinAPI.repository.WorkOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkOrderService {

    private WorkOrderRepository workOrderRepository;
    private CustomerRepository customerRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, CustomerRepository customerRepository) {
        this.workOrderRepository = workOrderRepository;
        this.customerRepository = customerRepository;
    }


    @Transactional
    public WorkOrderResponseDTO save(WorkOrderRequestDTO workOrderRequestDTO) {
        Optional<Customer> checkCustomer=customerRepository.findByEmail(workOrderRequestDTO.customerEmail());
        Customer customer;
        if(checkCustomer.isPresent()) {
            customer=checkCustomer.get();

        }
        else {
            customer=Mapper.toCustomer(workOrderRequestDTO);
        }
        customer=customerRepository.save(customer);
        WorkOrder workOrder=Mapper.toWorkOrder(workOrderRequestDTO,customer);
        workOrder=workOrderRepository.save(workOrder);
        return Mapper.toWorkOrderResponse(workOrder,customer);
    }
}
