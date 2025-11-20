package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Assignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.time.LocalDate;
import java.util.List;

@RepositoryDefinition(domainClass = Assignement.class, idClass = Long.class)
public interface AssignementRepository extends JpaRepository<Assignement, Long> {

    List<Assignement> findByRestaurantId(Long restaurantId);

    List<Assignement> findByEmployeeId(Long employeeId);

    List<Assignement> findByRestaurantIdAndEndDateBefore(
            Long restaurantId,
            LocalDate date
    );

    List<Assignement> findByEmployeeIdAndEndDateBefore(
            Long employeeId,
            LocalDate date
    );

}
