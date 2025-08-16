package com.Boghtech.MinAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;

@Entity
@Table(name="Work_ORDER_YAAM")
@Data
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // We should make sure that the ID column in the database is auto increment
    private Long id;

    @NotNull
    private String description;

    @Column(name="WO_Statues")
    private String workOrderStatues; // failed, completed

    @Column(name="CREATED_DATE")
    @NotNull
    private LocalDate createdAt; // cur date

    @Column(name="VISIT_DATE")
    private LocalDate visitDate; // first null

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id",nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technician_id",referencedColumnName = "id",nullable = true)
    private Technician technician;

}
