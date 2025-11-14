package com.gdu.wacdo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_address")
public class RestaurantAddress {

    public RestaurantAddress() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    public String city;

    @Column(nullable = false)
    public String postalCode;

    @Column(nullable = false)
    public String address;

    @Column(nullable = true)
    public float cordX;

    @Column(nullable = true)
    public float cordY;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getCordX() {
        return cordX;
    }

    public void setCordX(float cordX) {
        this.cordX = cordX;
    }

    public float getCordY() {
        return cordY;
    }

    public void setCordY(float cordY) {
        this.cordY = cordY;
    }
}
