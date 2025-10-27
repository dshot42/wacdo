package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "assignement") // nom de la table
public class Assignement {


    @EmbeddedId
    private AssignementId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    public LocalDate assignementDate;


    public AssignementId getId() {
        return id;
    }

    public void setId(AssignementId id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getAssignementDate() {
        return assignementDate;
    }

    public void setAssignementDate(LocalDate assignementDate) {
        this.assignementDate = assignementDate;
    }
}

