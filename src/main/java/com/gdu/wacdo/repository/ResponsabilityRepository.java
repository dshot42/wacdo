package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Responsability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Responsability.class, idClass = Long.class)
public interface ResponsabilityRepository extends JpaRepository<Responsability, Long> {

}
