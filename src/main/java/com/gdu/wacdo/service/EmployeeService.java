package com.gdu.wacdo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdu.wacdo.model.Assignement;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
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


    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Employee> getAll() {
        return repository.findAll();
    }

    public List<Object> getAll(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Employee> page = repository.findAll(pageable);
        return mapEmployee(page.getContent());
    }

    public Long countAll() {
        return repository.count();
    }


    public List<Object> findByRestaurant(String filter, String query, int limit, int offset, String order) {
        TypedQuery<Employee> typedQuery = getEmployeeTypedQuery(filter, query, order,null);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);
        return mapEmployee(typedQuery.getResultList());
    }

    public Map<String, Object> findByRestaurant(String filter, String query, String order, Long idRestaurant) {
        TypedQuery<Employee> typedQuery = getEmployeeTypedQuery(filter, query, order, idRestaurant);
        List<Employee> employees = typedQuery.getResultList();
        return mapEmployeeByRestaurant(employees, idRestaurant);
    }

    private TypedQuery<Employee> getEmployeeTypedQuery(String filter, String query, String order, Long idRestaurant) {
        String orderType = order == null ? "asc" : order;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> req = cb.createQuery(Employee.class);
        Root<Employee> employee = req.from(Employee.class);
        Join<Employee, Assignement> assignement = employee.join("assignements", JoinType.LEFT);
        Join<Assignement, Responsability> responsability = assignement.join("responsability", JoinType.LEFT);
        Join<Assignement, Restaurant> restaurant = assignement.join("restaurant", JoinType.LEFT);

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
        Predicate mailPredicate = cb.like(cb.lower(employee.get("mail")), pattern);

        Predicate queryPredicate;

        req.select(employee).distinct(true);

        switch (filter) {
            case "name" -> {
                queryPredicate = cb.like(cb.lower(employee.get("name")), pattern);
                req.orderBy(orderType.equals("asc") ? cb.asc(employee.get("name")) :
                        cb.desc(employee.get("name")));
            }
            case "surname" -> {
                queryPredicate = cb.like(cb.lower(employee.get("surname")), pattern);
                req.orderBy(orderType.equals("asc") ? cb.asc(employee.get("surname")) :
                        cb.desc(employee.get("surname")));
            }
            case "name+surname" -> {
                queryPredicate = cb.or(namePredicate1, namePredicate2);
                req.orderBy(orderType.equals("asc") ? cb.asc(employee.get("name")) :
                        cb.desc(employee.get("name")));
            }
            case "mail" -> {
                queryPredicate = mailPredicate;
                req.orderBy(orderType.equals("asc") ? cb.asc(employee.get("mail")) :
                        cb.desc(employee.get("mail")));
            }
            case "responsability" -> {
                queryPredicate = responsabilityPredicate;
                req.orderBy(orderType.equals("asc") ? cb.asc(responsability.get("role")) :
                        cb.desc(responsability.get("role")));
            }
            default -> {
                queryPredicate = cb.or(namePredicate1, namePredicate2, mailPredicate, restaurantPredicate, responsabilityPredicate);
                req.orderBy(orderType.equals("asc") ? cb.asc(employee.get("name")) :
                        cb.desc(employee.get("name")));
            }
        }

        if (idRestaurant != null)
            req.where(cb.and(cb.equal(restaurant.get("id"), idRestaurant), queryPredicate));
        else
            req.where(queryPredicate);
        return entityManager.createQuery(req);
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

        switch (filter) {
            case "name" -> req.where(cb.like(cb.lower(employee.get("name")), pattern));
            case "surname" -> req.where(cb.like(cb.lower(employee.get("surname")), pattern));
            case "name+surname" -> req.where(cb.or(namePredicate1, namePredicate2));
            case "mail" -> req.where(cb.like(cb.lower(employee.get("mail")), pattern));
            default -> req.where(cb.or(namePredicate1, namePredicate2, restaurantPredicate, responsabilityPredicate));
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

    public void saveEmployee(Employee employee) {
        if (employee.getId() == null) {
            repository.findByMail(employee.getMail())
                    .ifPresent(
                            e -> employee.setId(e.getId())
                    );
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        repository.save(employee);
    }


    public Map<String, Object> mapEmployeeByRestaurant(List<Employee> employees, Long idRestaurant) {
        Map<String, Object> map = new HashMap<>();
        List<Assignement> resAssignements = new LinkedList<>();
        List<Assignement> resOldAssignements = new LinkedList<>();

        employees.forEach(empl -> {
            Assignement ass = empl.getAssignements().stream().findFirst().orElse(null);
            if (ass.getEndDate() == null || ass.getEndDate().isAfter(LocalDate.now())) {
                resAssignements.add(ass);
            } else {
                resOldAssignements.add(ass);
            }
        });
        map.put("assignements", resAssignements);
        map.put("oldAssignements", resOldAssignements);

        return map;
    }

}
