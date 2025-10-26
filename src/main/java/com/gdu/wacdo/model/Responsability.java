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
    public String function;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
