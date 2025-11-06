package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    public RestaurantAddress restaurantAddress;


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // gère le côté Restaurant -> Assignement
    private List<Assignement> assignements = new ArrayList<>();

    @Column(columnDefinition = "text", nullable = true) // longtext pour MySQL , 1go pour PostgreSQL par default
    private String image;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
