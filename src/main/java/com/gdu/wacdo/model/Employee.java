package com.gdu.wacdo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name = "employee") // nom de la table
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String surname;

    @Column(nullable = false)
    public LocalDate hireDate;

    @Column(nullable = false)
    public String mail;

    @Column(nullable = false)
    public String phone;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "responsability_id")
    public Responsability responsability;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignement> assignements = new LinkedList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    public Role role;

}
