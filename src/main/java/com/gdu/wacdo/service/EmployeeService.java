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

import java.sql.SQLException;
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


    public List<Object> find(String filter, String query, boolean withoutAssignement, int limit, int offset, String order) throws SQLException {
        TypedQuery<Long> typedQuery = getEmployeeTypedQuery(filter, query, order, null, withoutAssignement);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);
        List<Employee> employees = repository.findAllById(typedQuery.getResultList());
        return mapEmployee(employees);
    }

    public Map<String, Object> findByRestaurant(String filter, String query, String order, Long idRestaurant) throws SQLException {
        TypedQuery<Long> typedQuery = getEmployeeTypedQuery(filter, query, order, idRestaurant, false);
        List<Employee> employees = repository.findAllById(typedQuery.getResultList());
        return mapEmployeeByRestaurant(employees, idRestaurant);
    }

    private TypedQuery<Long> getEmployeeTypedQuery(String filter, String query, String order, Long idRestaurant, boolean withoutAssignement) throws SQLException {
        try {
            String orderType = order == null ? "asc" : order;
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> req = cb.createQuery(Long.class);
            Root<Employee> employee = req.from(Employee.class);
            Join<Employee, Assignement> assignement = employee.join("assignements", JoinType.LEFT);
            Join<Assignement, Responsability> responsability = assignement.join("responsability", JoinType.LEFT);
            Join<Assignement, Restaurant> restaurant = assignement.join("restaurant", JoinType.LEFT);


            req.select(employee.get("id"));

            String pattern = "%" + query.toLowerCase() + "%";
            Predicate queryPredicate;

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


            switch (filter) {
                case "name" -> {
                    queryPredicate = cb.like(cb.lower(employee.get("name")), pattern);
                    req.orderBy(orderType.equals("asc") ? cb.asc(cb.min(employee.get("name"))) :
                            cb.desc(cb.min(employee.get("name"))));
                }
                case "surname" -> {
                    queryPredicate = cb.like(cb.lower(employee.get("surname")), pattern);
                    req.orderBy(orderType.equals("asc") ? cb.asc(cb.min(employee.get("surname"))) :
                            cb.desc(cb.min(employee.get("surname"))));
                }
                case "name+surname" -> {
                    queryPredicate = cb.or(namePredicate1, namePredicate2);
                    req.orderBy(orderType.equals("asc") ? cb.asc(cb.min(employee.get("name"))) :
                            cb.desc(cb.min(employee.get("name"))));
                }
                case "mail" -> {
                    queryPredicate = mailPredicate;
                    req.orderBy(orderType.equals("asc") ? cb.asc(cb.min(employee.get("mail"))) :
                            cb.desc(cb.min(employee.get("mail"))));
                }
                case "responsability" -> {
                    queryPredicate = responsabilityPredicate;
                    req.orderBy(orderType.equals("asc")
                            ? cb.asc(cb.min(responsability.get("role")))
                            : cb.desc(cb.min(responsability.get("role"))));
                }
                case "startedAt:Date" -> {
                    try {
                        queryPredicate = cb.equal(assignement.get("startDate"), LocalDate.parse(query));
                        ;
                    } catch (Exception e) {
                        throw new RuntimeException("[ERROR] startDate invalide format ", e);
                    }
                }
                default -> {
                    queryPredicate = cb.or(namePredicate1, namePredicate2, mailPredicate, restaurantPredicate, responsabilityPredicate);
                    req.orderBy(orderType.equals("asc") ? cb.asc(cb.min(employee.get("name"))) :
                            cb.desc(cb.min(employee.get("name"))));
                }
            }

            Predicate andPredicate = null;
            if (withoutAssignement) {
                andPredicate = cb.and(cb.isNull(assignement.get("id")), queryPredicate); // employee sans affectation
                queryPredicate = andPredicate;
            }
            if (idRestaurant != null) {
                andPredicate = cb.and(cb.equal(restaurant.get("id"), idRestaurant), queryPredicate);
                queryPredicate = andPredicate;
            }

            req.groupBy(employee.get("id"));
            req.where(queryPredicate);
            return entityManager.createQuery(req);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    public Long count(String filter, String query, boolean withoutAssignement) throws SQLException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> req = cb.createQuery(Long.class);
            Root<Employee> employee = req.from(Employee.class);
            Join<Employee, Assignement> assignement = employee.join("assignements", JoinType.LEFT);
            Join<Assignement, Responsability> responsability = assignement.join("responsability", JoinType.LEFT);
            Join<Assignement, Restaurant> restaurant = assignement.join("restaurant", JoinType.LEFT);
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
            Predicate queryPredicate;

            switch (filter) {
                case "name" -> queryPredicate = cb.like(cb.lower(employee.get("name")), pattern);
                case "surname" -> queryPredicate = cb.like(cb.lower(employee.get("surname")), pattern);
                case "name+surname" -> queryPredicate = cb.or(namePredicate1, namePredicate2);
                case "mail" -> queryPredicate = cb.like(cb.lower(employee.get("mail")), pattern);
                default ->
                        queryPredicate = cb.or(namePredicate1, namePredicate2, restaurantPredicate, responsabilityPredicate);
            }

            Predicate andPredicate = null;
            if (withoutAssignement) {
                andPredicate = cb.and(cb.isNull(assignement.get("id")), queryPredicate); // employee sans affectation
                queryPredicate = andPredicate;
            }
            Long idRestaurant = null;
            if (idRestaurant != null) {
                andPredicate = cb.and(cb.equal(restaurant.get("id"), idRestaurant), queryPredicate);
                queryPredicate = andPredicate;
            }

            req.where(queryPredicate);
            TypedQuery<Long> typedQuery = entityManager.createQuery(req);
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    public List<Object> mapEmployee(List<Employee> employees) {
        try {
            return employees.stream().map(e -> {
                try {
                    String json = objectMapper.writeValueAsString(e);
                    Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {
                    });

                    map.put("assignements", assignementRepository.findByEmployeeId(e.getId()).stream()
                            .filter(ass ->
                                    ass.getEndDate() == null || ass.getEndDate().isAfter(LocalDate.now())
                            ).toList());
                    return map;
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String saveEmployee(Employee employee) throws SQLException {
        try {

            Employee dbEmployee = (employee.getId() == null) ? null : repository.findById(employee.getId()).orElse(null);

            if (dbEmployee == null) {
                employee.setPassword(passwordEncoder.encode(employee.getPassword()));
                repository.save(employee);
                return "[SUCCESS] new Employee saved"; // <-- Thymeleaf fragment
            }

            // update => set hash pwd
            if (employee.getPassword() == null) {
                employee.setPassword(dbEmployee.getPassword());
            } else {
                employee.setPassword(
                        passwordEncoder.encode(employee.getPassword())
                );
            }
            repository.save(employee);
            return "[SUCCESS] update Employee saved";
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }


    public Map<String, Object> mapEmployeeByRestaurant(List<Employee> employees, Long idRestaurant) {
        Map<String, Object> map = new HashMap<>();
        List<Assignement> resAssignements = new LinkedList<>();
        List<Assignement> resOldAssignements = new LinkedList<>();

        employees.forEach(empl -> {
            Assignement ass = empl.getAssignements().stream()
                    .filter(a -> a.getRestaurant().getId().equals(idRestaurant))
                    .findFirst()
                    .orElse(null);
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
