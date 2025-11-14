package com.gdu.wacdo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    public ObjectMapper objectMapper;

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

    public List<Object> getAll(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Employee> page = repository.findAll(PageRequest.of(offset, limit));
        return mapEmployee(page.getContent());
    }

    public Long countAll() {
        return repository.count();
    }

    public List<Object> find(String filter, String query, int limit, int offset) {
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
        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        Predicate restaurantPredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate queryPredicate = cb.or(namePredicate1, namePredicate2, restaurantPredicate, responsabilityPredicate);

        req.select(employee).distinct(true);

        if (filter.equals("name")) {
            req.where(cb.like(cb.lower(employee.get("name")), pattern));
            req.orderBy(cb.asc(employee.get("name")));
        } else if (filter.equals("surname")) {
            req.where(cb.like(cb.lower(employee.get("surname")), pattern));
            req.orderBy(cb.asc(employee.get("surname")));
        } else if (filter.equals("name+surname")) {
            req.where(cb.or(namePredicate1, namePredicate2));
            req.orderBy(cb.asc(employee.get("name")));
        } else if (filter.equals("mail")) {
            req.where(cb.like(cb.lower(employee.get("mail")), pattern));
            req.orderBy(cb.asc(employee.get("mail")));
        } else {
            req.where(queryPredicate);
            req.orderBy(cb.asc(employee.get("name")));
        }

        TypedQuery<Employee> typedQuery = entityManager.createQuery(req);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);

        return mapEmployee(typedQuery.getResultList());
    }

    public Long count(String filter, String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Employee> employee = req.from(Employee.class);
        Join<Employee, Responsability> responsability = employee.join("assignements").join("responsability");
        Join<Employee, Restaurant> restaurant = employee.join("assignements").join("restaurant");
        String pattern = "%" + query.toLowerCase() + "%";

        req.select(cb.countDistinct(employee));

        Predicate namePredicate1 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("name")), " "),
                cb.lower(employee.get("surname"))
        ), pattern);
        Predicate namePredicate2 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("surname")), " "),
                cb.lower(employee.get("name"))
        ), pattern);
        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        Predicate restaurantPredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        //  Predicate hireDatePredicate = cb.equal(cb.lower(employee.get("hireDate")), pattern);

        if (filter.equals("name")) {
            req.where(cb.like(cb.lower(employee.get("name")), pattern));
        } else if (filter.equals("surname")) {
            req.where(cb.like(cb.lower(employee.get("surname")), pattern));
        } else if (filter.equals("name+surname")) {
            req.where(cb.or(namePredicate1, namePredicate2));
        } else if (filter.equals("mail")) {
            req.where(cb.like(cb.lower(employee.get("mail")), pattern));
        } else {
            req.where(cb.or(namePredicate1, namePredicate2, restaurantPredicate, responsabilityPredicate));
        }
        TypedQuery<Long> typedQuery = entityManager.createQuery(req);
        return typedQuery.getSingleResult();
    }

    public List<Object> mapEmployee(List<Employee> employees) {
        try {
            return employees.stream().map(e -> {
                try {
                    String json = objectMapper.writeValueAsString(e);
                    Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {
                    });
                    map.put("assignements", assignementRepository.findByEmployeeId(e.getId()));
                    return map;
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
