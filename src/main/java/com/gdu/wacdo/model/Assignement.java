package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "responsability_id")
    public Responsability responsability;


    public AssignementId getId() {
        return id;
    }

    public void setId(AssignementId id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Responsability getResponsability() {
        return responsability;
    }

    public void setResponsability(Responsability responsability) {
        this.responsability = responsability;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}