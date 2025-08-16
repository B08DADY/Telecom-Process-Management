package com.Boghtech.MinAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="TECHNICIAN_YAAM")
@Data
public class Technician {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String address;


    @OneToMany(mappedBy = "technician",cascade = CascadeType.ALL)
    private List<WorkOrder> workOrders;

}
