package com.gdu.wacdo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "responsability") // nom de la table
public class Responsability {

    public Responsability() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    public String role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
