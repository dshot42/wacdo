package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "employee") // nom de la table
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;


    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Assignement> assignements;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    private Role role;


    @Column(columnDefinition = "text", nullable = true)
    private String image;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean admin = false;

}