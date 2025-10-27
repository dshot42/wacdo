package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "restaurant") // nom de la table
public class Restaurant {

    public Restaurant() {
    }

    public Restaurant(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    public String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    public RestaurantAddress restaurantAddress;


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Assignement> assignements;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RestaurantAddress getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(RestaurantAddress restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public List<Assignement> getAssignements() {
        return assignements;
    }

    public void setAssignements(List<Assignement> assignements) {
        this.assignements = assignements;
    }
}
