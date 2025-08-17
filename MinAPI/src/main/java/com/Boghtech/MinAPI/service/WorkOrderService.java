package com.Boghtech.MinAPI.service;

import com.Boghtech.MinAPI.Mapper;
import com.Boghtech.MinAPI.dto.UpdateWorkOrderRequestDTO;
import com.Boghtech.MinAPI.dto.WorkOrderRequestDTO;
import com.Boghtech.MinAPI.dto.WorkOrderResponseDTO;
import com.Boghtech.MinAPI.exception.ResourceNotFoundException;
import com.Boghtech.MinAPI.model.Customer;
import com.Boghtech.MinAPI.model.TechSlot;
import com.Boghtech.MinAPI.model.Technician;
import com.Boghtech.MinAPI.model.WorkOrder;
import com.Boghtech.MinAPI.repository.CustomerRepository;
import com.Boghtech.MinAPI.repository.TechSlotRepository;
import com.Boghtech.MinAPI.repository.TechnicianRepository;
import com.Boghtech.MinAPI.repository.WorkOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {

    private WorkOrderRepository workOrderRepository;
    private CustomerRepository customerRepository;
    private TechnicianRepository technicianRepository;
    private TechSlotRepository techSlotRepository;
    public WorkOrderService(WorkOrderRepository workOrderRepository, CustomerRepository customerRepository,TechnicianRepository technicianRepository,TechSlotRepository TechSlotRepository) {
        this.workOrderRepository = workOrderRepository;
        this.customerRepository = customerRepository;
        this.technicianRepository=technicianRepository;
        this.techSlotRepository=TechSlotRepository;
    }


    @Transactional
    public WorkOrderResponseDTO save(WorkOrderRequestDTO workOrderRequestDTO) {

        if (workOrderRequestDTO.visitDate() != null) {
            LocalDate maxAllowedDate = LocalDate.now().plusDays(14);
            if (workOrderRequestDTO.visitDate().isAfter(maxAllowedDate)) {
                // Throw a clear, user-friendly exception that results in a 400 Bad Request.
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visit date cannot be more than 14 days in the future.");
            }
        }


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

    public void deleteById(Long id) {

        if (!workOrderRepository.existsById(id)) {

            throw new ResourceNotFoundException("WorkOrder with ID '" + id + "' not found.");
        }


        workOrderRepository.deleteById(id);
    }

    public List<WorkOrderResponseDTO> findAll() {
        return workOrderRepository.findAll()
                .stream()
                .map(workOrder -> Mapper.toWorkOrderResponse(workOrder, workOrder.getCustomer()))
                .collect(Collectors.toList());
    }
    public List<WorkOrderResponseDTO> findUnassigned() {
        return workOrderRepository.findByTechnicianIsNull()
                .stream()
                .map(workOrder -> Mapper.toWorkOrderResponse(workOrder, workOrder.getCustomer()))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkOrderResponseDTO assignTechnician(Long workOrderId, Long technicianId) {

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID " + workOrderId + " not found."));


        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician with ID " + technicianId + " not found."));


        workOrder.setTechnician(technician);


        WorkOrder updatedWorkOrder = workOrderRepository.save(workOrder);


        return Mapper.toWorkOrderResponse(updatedWorkOrder, updatedWorkOrder.getCustomer());
    }
    @Transactional
    public WorkOrderResponseDTO updateWorkOrder(Long workOrderId, UpdateWorkOrderRequestDTO dto) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID " + workOrderId + " not found."));


        Customer customer = workOrder.getCustomer();

        if (dto.workOrderDescription() != null) {
            workOrder.setDescription(dto.workOrderDescription());
        }

        if (dto.customerName() != null) {
            customer.setName(dto.customerName());
        }
        if (dto.customerEmail() != null) {
            customer.setEmail(dto.customerEmail());
        }
        if (dto.customerPhone() != null) {
            customer.setPhone(dto.customerPhone());
        }
        if (dto.customerAddress() != null) {
            customer.setAddress(dto.customerAddress());
        }


        return Mapper.toWorkOrderResponse(workOrder, customer);
    }

    @Transactional
    public WorkOrderResponseDTO reassignTechnician(Long workOrderId, Long newTechnicianId) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID '" + workOrderId + "' not found."));

        Technician newTechnician = technicianRepository.findById(newTechnicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician with ID '" + newTechnicianId + "' not found."));

        Optional<TechSlot> associatedSlot = techSlotRepository.findByWorkOrder(workOrder);
        associatedSlot.ifPresent(techSlotRepository::delete);


        workOrder.setTechnician(newTechnician);
        workOrder.setVisitDate(null);


        WorkOrder updatedWorkOrder = workOrderRepository.save(workOrder);


        return Mapper.toWorkOrderResponse(updatedWorkOrder, updatedWorkOrder.getCustomer());
    }


}
