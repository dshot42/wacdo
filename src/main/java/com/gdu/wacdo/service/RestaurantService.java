package com.gdu.wacdo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdu.wacdo.model.*;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public EntityManager entityManager;

    @Autowired
    public RestaurantRepository repository;

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public AssignementRepository assignementRepository;


    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public List<Object> getAll(int limit, int offset) {
        Page<Restaurant> page = repository.findAll(PageRequest.of(offset, limit));
        return mapRestaurants(page.getContent());
    }

    public Long countAll() {
        return repository.count();
    }

    public List<Object> find(String filter, String query, boolean withoutAssignement, int limit, int offset, String order) {
        TypedQuery<Long> typedQuery = getRestaurantTypedQuery(filter, query, order, null, withoutAssignement);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);
        List<Restaurant> employees = repository.findAllById(typedQuery.getResultList());
        return mapRestaurants(employees);
    }

    public Map<String, Object> findByEmployee(String filter, String query, String order, Long idEmployee) {
        TypedQuery<Long> typedQuery = getRestaurantTypedQuery(filter, query, order, idEmployee, false);
        List<Restaurant> restaurants = repository.findAllById(typedQuery.getResultList());
        return mapRestaurantByEmployee(restaurants, idEmployee);
    }

    public TypedQuery<Long> getRestaurantTypedQuery(String filter, String query, String order, Long idEmployee, boolean withoutAssignement) {
        String orderType = order == null ? "asc" : order;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Restaurant> restaurant = req.from(Restaurant.class);
        Join<Restaurant, RestaurantAddress> address = restaurant.join("restaurantAddress");
        Join<Restaurant, Assignement> assignement = restaurant.join("assignements", JoinType.LEFT);
        Join<Assignement, Responsability> responsability = assignement.join("responsability", JoinType.LEFT);
        Join<Assignement, Employee> employee = assignement.join("employee", JoinType.LEFT);

        String pattern = "%" + query.toLowerCase() + "%";

        req.select(restaurant.get("id"));

        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        Predicate postalPredicate = cb.like(cb.lower(address.get("postalCode")), pattern);
        Predicate namePredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate addrPredicate = cb.like(cb.lower(address.get("address")), pattern);
        Predicate cityPredicate = cb.like(cb.lower(address.get("city")), pattern);

        Predicate queryPredicate;
        switch (filter) {
            case "name" -> {
                queryPredicate = namePredicate;
                req.orderBy(cb.asc(cb.min(restaurant.get("name"))));
            }
            case "city" -> {
                queryPredicate = cityPredicate;
                req.orderBy(cb.asc(cb.min(address.get("city"))));
            }
            case "responsability" -> {
                queryPredicate = responsabilityPredicate;
                req.orderBy(orderType.equals("asc")
                        ? cb.asc(cb.min(responsability.get("role")))
                        : cb.desc(cb.min(responsability.get("role"))));
            }
            case "postalCode" -> queryPredicate = postalPredicate;
            default ->
                    queryPredicate = cb.or(namePredicate, addrPredicate, cityPredicate, postalPredicate, responsabilityPredicate);
        }

        Predicate andPredicate = null;
        if (withoutAssignement) {
            andPredicate = cb.and(cb.isNull(assignement.get("id")), queryPredicate); // employee sans affectation
            queryPredicate = andPredicate;
        }
        if (idEmployee != null) {
            andPredicate = cb.and(cb.equal(employee.get("id"), idEmployee), queryPredicate);
            queryPredicate = andPredicate;
        }

        req.groupBy(restaurant.get("id"));
        req.where(queryPredicate);
        return entityManager.createQuery(req);
    }

    public Long count(String filter, String query, boolean withoutAssignement) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Restaurant> restaurant = req.from(Restaurant.class);
        Join<Restaurant, RestaurantAddress> address = restaurant.join("restaurantAddress");
        Join<Restaurant, Assignement> assignement = restaurant.join("assignements", JoinType.LEFT);
        Join<Assignement, Responsability> responsability = assignement.join("responsability", JoinType.LEFT);
        Join<Assignement, Employee> employee = assignement.join("employee", JoinType.LEFT);

        String pattern = "%" + query.toLowerCase() + "%";

        req.select(cb.countDistinct(restaurant));

        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        Predicate postalPredicate = cb.like(cb.lower(address.get("postalCode")), pattern);
        Predicate namePredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate addrPredicate = cb.like(cb.lower(address.get("address")), pattern);
        Predicate cityPredicate = cb.like(cb.lower(address.get("city")), pattern);

        Predicate queryPredicate;
        switch (filter) {
            case "name" -> {
                queryPredicate = namePredicate;
            }
            case "city" -> {
                queryPredicate = cityPredicate;
            }
            case "responsability" -> {
                queryPredicate = responsabilityPredicate;

            }
            case "postalCode" -> queryPredicate = postalPredicate;
            default ->
                    queryPredicate = cb.or(namePredicate, addrPredicate, cityPredicate, postalPredicate, responsabilityPredicate);
        }

        if (withoutAssignement) {
            queryPredicate = cb.and(cb.isNull(assignement.get("id")), queryPredicate); // employee sans affectation
        }

        req.where(queryPredicate);
        TypedQuery<Long> typedQuery = entityManager.createQuery(req);
        return typedQuery.getSingleResult();
    }


    public List<Object> mapRestaurants(List<Restaurant> restaurants) {
        try {
            return restaurants.stream().map(r -> {
                try {
                    String json = objectMapper.writeValueAsString(r);
                    Map<String, Object> map = objectMapper.readValue(json, new TypeReference<>() {
                    });

                    List<Assignement> assignements = assignementRepository.findByRestaurantId(r.getId());
                    List<Assignement> oldAssignements = assignementRepository.findByRestaurantIdAndEndDateBefore(r.getId(), LocalDate.now());
                    assignements.removeAll(oldAssignements);

                    map.put("assignements", assignements);
                    map.put("oldAssignements", oldAssignements);

                    return map;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Map<String, Object> mapRestaurantByEmployee(List<Restaurant> restaurants, Long idEmployee) {
        Map<String, Object> map = new HashMap<>();
        List<Assignement> resAssignements = new LinkedList<>();
        List<Assignement> resOldAssignements = new LinkedList<>();

        restaurants.forEach(res -> {
            Assignement ass = res.getAssignements().stream().filter(a -> a.getEmployee().getId().equals(idEmployee))
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
