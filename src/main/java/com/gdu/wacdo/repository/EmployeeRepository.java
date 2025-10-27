package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;


@RepositoryDefinition(domainClass = Employee.class, idClass = Long.class)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
