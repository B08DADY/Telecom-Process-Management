package com.Boghtech.MinAPI.service;

import com.Boghtech.MinAPI.Mapper;
import com.Boghtech.MinAPI.dto.*;
import com.Boghtech.MinAPI.exception.ResourceConflictException;
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
import org.springframework.dao.DataIntegrityViolationException;
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

//        if (workOrderRequestDTO.visitDate() != null) {
//            LocalDate maxAllowedDate = LocalDate.now().plusDays(14);
//            if (workOrderRequestDTO.visitDate().isAfter(maxAllowedDate)) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visit date cannot be more than 14 days in the future.");
//            }
//        }
        Customer customer;
        if(workOrderRequestDTO.customerEmail()!=null) {
            Optional<Customer> checkCustomer = customerRepository.findByEmail(workOrderRequestDTO.customerEmail());

            if (checkCustomer.isPresent()) {
                customer = checkCustomer.get();

            } else {
                customer = Mapper.toCustomer(workOrderRequestDTO);
            }
        }
        else{
            customer=Mapper.toCustomer(workOrderRequestDTO);
        }

        customer=customerRepository.save(customer);
        WorkOrder workOrder=Mapper.toWorkOrder(workOrderRequestDTO,customer);
        workOrder.setWorkOrderStatues("New");
        workOrder=workOrderRepository.save(workOrder);
        return Mapper.toWorkOrderResponse(workOrder,customer);
    }

    @Transactional
    public WorkOrderResponseDTO closeWorkOrder(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID '" + id + "' not found."));


        workOrder.setWorkOrderStatues("CLOSED");

        techSlotRepository.findByWorkOrder(workOrder).ifPresent(slot -> {
            techSlotRepository.delete(slot);
        });
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        return Mapper.toWorkOrderResponse(savedWorkOrder, savedWorkOrder.getCustomer());
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
    public WorkOrderResponseDTO assignTechnicianAndCreateSlot(Long workOrderId, AssignTechnicianRequestDTO assignRequest) {
        // 1. Find the WorkOrder or throw a 404 error
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID '" + workOrderId + "' not found."));


//        techSlotRepository.findByWorkOrder(workOrder)
//                .ifPresent(techSlotRepository::delete);

        // 2. Find the Technician or throw a 404 error
        Technician technician = technicianRepository.findById(assignRequest.technicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Technician with ID '" + assignRequest.technicianId() + "' not found."));

        // 3. Business Rule: Check if the WorkOrder is already assigned/scheduled
        if (workOrder.getTechnician()!=null || workOrder.getWorkOrderStatues().equals("ASSIGNED")) {
            throw new ResourceConflictException("WorkOrder with ID '" + workOrderId + "' is already assigned or scheduled.");
        }

        // 4. Update the WorkOrder details
        workOrder.setTechnician(technician);
        workOrder.setWorkOrderStatues("ASSIGNED");
        workOrder.setVisitDate(assignRequest.visitDate());
        // 5. Create the new TechSlot entity with a NULL slot
        TechSlot newSlot = new TechSlot(
                null,
                technician,
                workOrder,
                assignRequest.visitDate(),
                null // <-- Passing NULL for the slot
        );

        // 6. Save both entities in one transaction
        try {
            TechSlot savedSlot = techSlotRepository.save(newSlot);

        } catch (RuntimeException e) {

            throw new ResourceConflictException("This technician is already booked for this specific date and time slot.");
        }
        workOrderRepository.save(workOrder);

        // 7. Return the updated work order
        return Mapper.toWorkOrderResponse(workOrder, workOrder.getCustomer());
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
    public WorkOrderResponseDTO reassignTechnician(Long workOrderId, ReAssignTechnicianRequestDTO reassignRequest) {
        // 1. Find the essential entities, or throw 404 errors.
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID '" + workOrderId + "' not found."));

        Technician newTechnician = technicianRepository.findById(reassignRequest.newTechnicianId())
                .orElseThrow(() -> new ResourceNotFoundException("New Technician with ID '" + reassignRequest.newTechnicianId() + "' not found."));

        // 2. Find the existing schedule slot for this work order.
        // If it's not assigned/scheduled, it can't be reassigned.
        TechSlot existingSlot = techSlotRepository.findByWorkOrder(workOrder)
                .orElseThrow(() -> new ResourceNotFoundException("No schedule found for WorkOrder with ID '" + workOrderId + "'. It cannot be reassigned."));

        // 3. Perform the updates as per your new logic.

        // Update the WorkOrder's technician reference.
        workOrder.setTechnician(newTechnician);
        workOrder.setWorkOrderStatues("ASSIGNED"); // Status is now 'ASSIGNED', not 'SCHEDULED'.

        // Update the TechSlot: change the technician and set the time slot to null.
        // The visitDate remains the same.
        existingSlot.setTechnician(newTechnician);
        existingSlot.setSlot(null); // <-- Setting the specific time slot to null

        // 4. The @Transactional annotation ensures both the updated workOrder and
        // existingSlot are saved to the database automatically. No explicit .save() calls are needed.

        // 5. Return the updated work order details.
        return Mapper.toWorkOrderResponse(workOrder, workOrder.getCustomer());
    }
    @Transactional
    public WorkOrderResponseDTO RescheduleWorkOrder(Long workOrderId, AssignTechnicianRequestDTO assignRequest)
    {
        // 1. Find the essential entities, or throw 404 errors.
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkOrder with ID '" + workOrderId + "' not found."));
        // 2. Find the Technician or throw a 404 error
        Technician technician = technicianRepository.findById(assignRequest.technicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Technician with ID '" + assignRequest.technicianId() + "' not found."));
      if(!workOrder.getWorkOrderStatues().equals("ASSIGNED"))
      {
          throw new ResourceConflictException("WorkOrder with ID '" + workOrderId + "' is not Assigned.");
      }
        workOrder.setTechnician(technician);
        workOrder.setVisitDate(assignRequest.visitDate());
        workOrder.setWorkOrderStatues("ASSIGNED");

        TechSlot newSlot = new TechSlot(
                null,
                technician,
                workOrder,
                assignRequest.visitDate(),
                null // <-- Passing NULL for the slot
        );

        // 6. Save both entities in one transaction
        try {
            TechSlot savedSlot = techSlotRepository.save(newSlot);

        } catch (RuntimeException e) {

            throw new ResourceConflictException("This technician is already booked for this specific date and time slot.");
        }
        workOrderRepository.save(workOrder);
        // 7. Return the updated work order
        return Mapper.toWorkOrderResponse(workOrder, workOrder.getCustomer());


    }

    }
