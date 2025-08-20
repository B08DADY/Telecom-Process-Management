package com.Boghtech.MinAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
// The uniqueConstraints attribute has been completely removed from @Table.
@Table(name = "TECH_SLOT_YAAM")
@Getter // Replaced @Data with specific, safe annotations for JPA
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tech_slot_seq_generator")
    @SequenceGenerator(name = "tech_slot_seq_generator", sequenceName = "TECH_SLOT_YAAM_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id", nullable = false)
    private Technician technician;

    @OneToOne(fetch = FetchType.LAZY)
    // This "unique = true" correctly represents the ONLY unique rule (besides the PK)
    // that is left on the table.
    @JoinColumn(name = "work_order_id", nullable = false, unique = true)
    private WorkOrder workOrder;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "slot", nullable = true)
    private Integer slot;
}