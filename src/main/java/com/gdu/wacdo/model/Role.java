package com.gdu.wacdo.model;


import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "role") // nom de la table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrément
    private Long id;

    @Column(nullable = false)
    public String roleName;

    @ManyToMany
    @JoinTable(
            name = "permission",                       // table de liaison
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "access_id")
    )
    private List<Access> accesses = new LinkedList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Access> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<Access> accesses) {
        this.accesses = accesses;
    }
}
