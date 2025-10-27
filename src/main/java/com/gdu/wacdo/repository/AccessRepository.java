package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Access;
import com.gdu.wacdo.model.Responsability;
import com.gdu.wacdo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Access.class, idClass = Long.class)
public interface AccessRepository extends JpaRepository<Access, Long> {

}
