package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    public RestaurantAddress restaurantAddress;

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<Assignement> assignements;

    @Column(columnDefinition = "text", nullable = true)
    private String image;

}