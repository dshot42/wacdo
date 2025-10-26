package com.gdu.wacdo.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "assignement") // nom de la table
public class Assignement {

    @EmbeddedId
    private AssignementId id;

    @MapsId("employeeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @MapsId("restaurantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    public LocalDate assignementDate;
}

