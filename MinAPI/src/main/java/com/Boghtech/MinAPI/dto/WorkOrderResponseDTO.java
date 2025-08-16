package com.Boghtech.MinAPI.dto;

public record WorkOrderResponseDTO(
        String workOrderId,
        String workOrderDescription,
        String customerID,
        String customerName,
        String customerEmail,
        String customerPhone,
        String customerAddress,
        String createdAt
        ) {


}
