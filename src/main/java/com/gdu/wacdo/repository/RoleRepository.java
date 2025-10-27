package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Responsability;
import com.gdu.wacdo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Role.class, idClass = Long.class)
public interface RoleRepository extends JpaRepository<Role, Long> {

}
