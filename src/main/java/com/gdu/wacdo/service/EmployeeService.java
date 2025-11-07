package com.gdu.wacdo.service;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.model.Responsability;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AssignementRepository assignementRepository;

    @Autowired
    public EmployeeRepository repository;


    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public List<Employee> getAll(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Employee> page = repository.findAll(PageRequest.of(offset, limit));
        return page.getContent();
    }

    public Long countAll() {
        return repository.count();
    }

    public List<Employee> find(String query, int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> req = cb.createQuery(Employee.class);
        Root<Employee> employee = req.from(Employee.class);
        Join<Employee, Responsability> responsability = employee.join("assignements").join("responsability");
        Join<Employee, Restaurant> restaurant = employee.join("assignements").join("restaurant");

        String pattern = "%" + query.toLowerCase() + "%";

        Predicate namePredicate1 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("name")), " "),
                cb.lower(employee.get("surname"))
        ), pattern);
        Predicate namePredicate2 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("surname")), " "),
                cb.lower(employee.get("name"))
        ), pattern);
        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("function")), pattern);
        Predicate restaurantPredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        //   Predicate hireDatePredicate = cb.equal(cb.lower(employee.get("hireDate")), pattern);
        Predicate queryPredicate = cb.or(namePredicate1, namePredicate2, restaurantPredicate, responsabilityPredicate);
        req.where(queryPredicate);
        TypedQuery<Employee> typedQuery = entityManager.createQuery(req);
        typedQuery.setFirstResult(offset * limit);  // OFFSET
        typedQuery.setMaxResults(limit);    // LIMIT

        return typedQuery.getResultList();
    }

    public Long count(String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Employee> employee = req.from(Employee.class);
        Join<Employee, Responsability> responsability = employee.join("assignements").join("responsability");
        Join<Employee, Restaurant> restaurant = employee.join("assignements").join("restaurant");

        String pattern = "%" + query.toLowerCase() + "%";

        Predicate namePredicate1 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("name")), " "),
                cb.lower(employee.get("surname"))
        ), pattern);
        Predicate namePredicate2 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("surname")), " "),
                cb.lower(employee.get("name"))
        ), pattern);
        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("function")), pattern);
        Predicate restaurantPredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        //  Predicate hireDatePredicate = cb.equal(cb.lower(employee.get("hireDate")), pattern);

        Predicate predicate = cb.or(namePredicate1, namePredicate2, restaurantPredicate, responsabilityPredicate);
        req.select(cb.count(employee));
        req.where(predicate);
        TypedQuery<Long> typedQuery = entityManager.createQuery(req);

        return typedQuery.getSingleResult();
    }


}
