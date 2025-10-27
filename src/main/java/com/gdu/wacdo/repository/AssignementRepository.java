package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Assignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Assignement.class, idClass = Long.class)
public interface AssignementRepository extends JpaRepository<Assignement, Long> {

}
