package com.gdu.wacdo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdu.wacdo.model.Assignement;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.model.RestaurantAddress;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.EmployeeRepository;
import com.gdu.wacdo.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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


    public List<Object> find(String filter, String query, int limit, int offset) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> req = cb.createQuery(Restaurant.class);
        Root<Restaurant> restaurant = req.from(Restaurant.class);
        Join<Restaurant, RestaurantAddress> address = restaurant.join("restaurantAddress");
        String pattern = "%" + query.toLowerCase() + "%";

        req.select(restaurant).distinct(true);

        Predicate postalPredicate = cb.like(cb.lower(address.get("postalCode")), pattern);
        Predicate namePredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate addrPredicate = cb.like(cb.lower(address.get("address")), pattern);
        Predicate cityPredicate = cb.like(cb.lower(address.get("city")), pattern);

        switch (filter) {
            case "name" -> {
                req.where(namePredicate);
                req.orderBy(cb.asc(restaurant.get("name")));
            }
            case "city" -> {
                req.where(cityPredicate);
                req.orderBy(cb.asc(address.get("city")));
            }
            case "postalCode" -> req.where(postalPredicate);
            default -> req.where(cb.or(namePredicate, addrPredicate, cityPredicate, postalPredicate));
        }

        req.orderBy(cb.asc(restaurant.get("name")));
        TypedQuery<Restaurant> typedQuery = entityManager.createQuery(req);
        typedQuery.setFirstResult(offset * limit);  // OFFSET
        typedQuery.setMaxResults(limit);    // LIMIT
        return mapRestaurants(typedQuery.getResultList());
    }

    public Long count(String filter, String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Restaurant> restaurant = req.from(Restaurant.class);
        Join<Restaurant, RestaurantAddress> address = restaurant.join("restaurantAddress");
        String pattern = "%" + query.toLowerCase() + "%";

        req.select(cb.countDistinct(restaurant));

        Predicate postalPredicate = cb.like(cb.lower(address.get("postalCode")), pattern);
        Predicate namePredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
        Predicate addrPredicate = cb.like(cb.lower(address.get("address")), pattern);
        Predicate cityPredicate = cb.like(cb.lower(address.get("city")), pattern);

        switch (filter) {
            case "name" -> req.where(namePredicate);
            case "city" -> req.where(cityPredicate);
            case "postalCode" -> req.where(postalPredicate);
            default -> req.where(cb.or(namePredicate, addrPredicate, cityPredicate, postalPredicate));
        }

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

}
