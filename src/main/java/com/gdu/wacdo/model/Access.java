package com.gdu.wacdo.model;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "access") // nom de la table
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String path;

    @ManyToMany(mappedBy = "accesses")
    private List<Role> roles = new LinkedList<>();

}
