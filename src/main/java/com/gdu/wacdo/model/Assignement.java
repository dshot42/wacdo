package com.gdu.wacdo.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "assignement")
public class Assignement {


    @EmbeddedId
    private AssignementId id;

    @ManyToOne
    @MapsId("employeeId") // lie l'EmbeddedId.employeeId
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("restaurantId") // lie l'EmbeddedId.restaurantId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    public LocalDate startDate;

    @Column(nullable = true)
    public LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "responsability_id")
    public Responsability responsability;


}