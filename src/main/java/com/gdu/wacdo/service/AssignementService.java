package com.gdu.wacdo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdu.wacdo.model.*;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.ResponsabilityRepository;
import com.gdu.wacdo.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AssignementService {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;


    @Autowired
    AssignementRepository repository;

    @Autowired
    EmployeeService employeeService;


    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ResponsabilityRepository responsabilityRepository;


    public List<Assignement> getAll() {
        return repository.findAll();
    }

    public List<Assignement> getAll(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Assignement> page = repository.findAll(PageRequest.of(offset, limit));
        return page.getContent();
    }

    public Long countAll() {
        return repository.count();
    }

    public List<Assignement> find(String filter, String query, int limit, int offset) {
        CriteriaQuery<Assignement> req = this.getAssignementCriteriaQuery(filter, query);
        TypedQuery<Assignement> typedQuery = entityManager.createQuery(req);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);

        return typedQuery.getResultList();
    }

    private CriteriaQuery<Assignement> getAssignementCriteriaQuery(String filter, String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Assignement> req = cb.createQuery(Assignement.class);
        Root<Assignement> assignement = req.from(Assignement.class);
        Join<Assignement, Responsability> responsability = assignement.join("responsability");
        Join<Assignement, Restaurant> restaurant = assignement.join("restaurant");
        Join<Restaurant, RestaurantAddress> restaurantAddress = assignement.join("restaurant").join("restaurantAddress");
        Join<Assignement, Employee> employee = assignement.join("employee");

        String pattern = "%" + query.toLowerCase() + "%";

        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        Predicate restaurantPredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate restaurantAddressPredicate = cb.like(cb.lower(restaurantAddress.get("city")), pattern);
        Predicate employeePredicate1 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("name")), " "),
                cb.lower(employee.get("surname"))
        ), pattern);
        Predicate employeePredicate2 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("surname")), " "),
                cb.lower(employee.get("name"))
        ), pattern);

        req.select(assignement).distinct(true);

        switch (filter) {
            case "restaurant_name" -> {
                req.where(cb.like(cb.lower(restaurant.get("name")), pattern));
            }
            case "employee_name" -> {
                req.where(cb.or(employeePredicate1, employeePredicate2));
            }
            case "role" -> {
                req.where(responsabilityPredicate);
            }
            case "city" -> {
                req.where(restaurantAddressPredicate);
            }
            case "startDate" -> req.where(cb.equal(assignement.get("startDate"), LocalDate.parse(query)));
            case "endDate" -> req.where(cb.equal(assignement.get("endDate"), LocalDate.parse(query)));
            default ->
                    req.where(cb.or(employeePredicate1, employeePredicate2, restaurantPredicate, responsabilityPredicate, restaurantAddressPredicate));
        }
        return req;
    }

    public Long count(String filter, String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Assignement> assignement = req.from(Assignement.class);
        Join<Assignement, Responsability> responsability = assignement.join("responsability");
        Join<Assignement, Restaurant> restaurant = assignement.join("restaurant");
        Join<Restaurant, RestaurantAddress> restaurantAddress = assignement.join("restaurant").join("restaurantAddress");
        Join<Assignement, Employee> employee = assignement.join("employee");

        String pattern = "%" + query.toLowerCase() + "%";

        req.select(cb.countDistinct(assignement));

        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        Predicate restaurantPredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate restaurantAddressPredicate = cb.like(cb.lower(restaurantAddress.get("city")), pattern);
        Predicate employeePredicate1 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("name")), " "),
                cb.lower(employee.get("surname"))
        ), pattern);
        Predicate employeePredicate2 = cb.like(cb.concat(
                cb.concat(cb.lower(employee.get("surname")), " "),
                cb.lower(employee.get("name"))
        ), pattern);

        switch (filter) {
            case "restaurant_name" -> req.where(cb.like(cb.lower(restaurant.get("name")), pattern));
            case "employee_name" -> req.where(cb.or(employeePredicate1, employeePredicate2));
            case "role" -> req.where(responsabilityPredicate);
            case "city" -> req.where(restaurantAddressPredicate);
            case "startDate" -> req.where(cb.equal(assignement.get("startDate"), LocalDate.parse(query)));
            case "endDate" -> req.where(cb.equal(assignement.get("endDate"), LocalDate.parse(query)));
            default ->
                    req.where(cb.or(employeePredicate1, employeePredicate2, restaurantPredicate, responsabilityPredicate, restaurantAddressPredicate));
        }
        TypedQuery<Long> typedQuery = entityManager.createQuery(req);
        return typedQuery.getSingleResult();
    }


    public Map<String, Object> findEmployeeByRestaurant(String query, String filter, Long idRestaurant) {
        Map<String, Object> map = new HashMap<>();
        List<Assignement> resAssignements = new LinkedList<>();
        List<Assignement> resOldAssignements = new LinkedList<>();

        List<Assignement> restaurantAssignements = repository.findByRestaurantId(idRestaurant);
        List<Employee> employees = employeeService.find(filter, query);

        restaurantAssignements.forEach(ass -> {
            if (employees.stream().anyMatch(employee -> employee.getId().equals(ass.getEmployee().getId()))) {
                if (ass.getEndDate() == null || ass.getEndDate().isAfter(LocalDate.now())) {
                    resAssignements.add(ass);
                } else {
                    resOldAssignements.add(ass);
                }
            }
        });

        map.put("assignements", resAssignements);
        map.put("oldAssignements", resOldAssignements);

        return map;
    }


    public void save(Assignement assignement) throws Exception {
        Employee emp = employeeService.repository.findById(assignement.getId().getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Restaurant res = restaurantRepository.findById(assignement.getId().getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        assignement.setEmployee(emp);
        assignement.setRestaurant(res);

        Responsability resp = responsabilityRepository.findById(assignement.getResponsability().getId())
                .orElseThrow(() -> new RuntimeException("Responsability not found"));
        assignement.setResponsability(resp);

        repository.save(assignement);
    }

}
