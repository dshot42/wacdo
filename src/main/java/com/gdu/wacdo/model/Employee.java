package com.gdu.wacdo.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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
    @JsonManagedReference
    private List<Assignement> assignements = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id")
    public Role role;


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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Responsability getResponsability() {
        return responsability;
    }

    public void setResponsability(Responsability responsability) {
        this.responsability = responsability;
    }

    public List<Assignement> getAssignements() {
        return assignements;
    }

    public void setAssignements(List<Assignement> assignements) {
        this.assignements = assignements;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
