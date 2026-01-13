package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    @OneToMany(mappedBy = "responsability")
    @JsonIgnore
    List<Assignement> assignements;


}
