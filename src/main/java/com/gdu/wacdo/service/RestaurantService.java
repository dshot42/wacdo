package com.gdu.wacdo.service;

import com.gdu.wacdo.model.Assignement;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.model.RestaurantAddress;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AssignementRepository assignementRepository;

    @Autowired
    public RestaurantRepository repository;


    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public List<Restaurant> getAll( int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Restaurant> page = repository.findAll(PageRequest.of(offset, limit));
        return page.getContent();
    }

    public Long countAll() {
        return repository.count();
    }

    public List<Restaurant> find(String query, int limit, int offset) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Restaurant> req = cb.createQuery(Restaurant.class);
            Root<Restaurant> restaurant = req.from(Restaurant.class);
            Join<Restaurant, RestaurantAddress> address = restaurant.join("restaurantAddress");
            String pattern = "%" + query.toLowerCase() + "%";

              if ( query.matches("\\d+")) {
                Predicate postalPredicate = cb.equal(address.get("postalCode"), Integer.parseInt(query));
                req.where(cb.or(postalPredicate));
            } else {
                  Predicate namePredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
                  Predicate addrPredicate = cb.like(cb.lower(address.get("address")), pattern);
                  Predicate cityPredicate = cb.like(cb.lower(address.get("city")), pattern);
                  req.where(cb.or(namePredicate, addrPredicate, cityPredicate));
            }

            TypedQuery<Restaurant> typedQuery = entityManager.createQuery(req);
            typedQuery.setFirstResult(offset*limit);  // OFFSET
            typedQuery.setMaxResults(limit);    // LIMIT

            return typedQuery.getResultList();
    }

    public Long count(String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Restaurant> restaurant = req.from(Restaurant.class);
        Join<Restaurant, RestaurantAddress> address = restaurant.join("restaurantAddress");
        String pattern = "%" + query.toLowerCase() + "%";

        Predicate predicate;
        if (query.matches("\\d+")) {
            predicate = cb.equal(address.get("postalCode"), Integer.parseInt(query));
        } else {
            Predicate namePredicate = cb.like(cb.lower(restaurant.get("name")), pattern);
            Predicate addrPredicate = cb.like(cb.lower(address.get("address")), pattern);
            Predicate cityPredicate = cb.like(cb.lower(address.get("city")), pattern);
            predicate = cb.or(namePredicate, addrPredicate, cityPredicate);
        }

        req.select(cb.count(restaurant));
        req.where(predicate);
        TypedQuery<Long> typedQuery = entityManager.createQuery(req);

        return typedQuery.getSingleResult();
    }


}
