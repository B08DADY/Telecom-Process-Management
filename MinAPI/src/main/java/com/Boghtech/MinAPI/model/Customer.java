package com.Boghtech.MinAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name="CUSTOMER_YAAM")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique=true)
    @Email
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String address;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<WorkOrder> workOrders;


}
