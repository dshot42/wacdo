package com.gdu.wacdo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdu.wacdo.model.Assignement;
import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.model.Responsability;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.ResponsabilityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponsabilityService {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AssignementRepository assignementRepository;

    @Autowired
    public ResponsabilityRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<Object> find(String filter, String query, int limit, int offset, String order) {
        TypedQuery<Responsability> typedQuery = getReponsabilityTypedQuery(filter, query, order);
        typedQuery.setFirstResult(offset * limit);
        typedQuery.setMaxResults(limit);
        return mapResponsability(typedQuery.getResultList());
    }

    private TypedQuery<Responsability> getReponsabilityTypedQuery(String filter, String query, String order) {
        String orderType = order == null ? "asc" : order;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Responsability> req = cb.createQuery(Responsability.class);
        Root<Responsability> responsability = req.from(Responsability.class);
        Join<Responsability, Assignement> assignement = responsability.join("assignements", JoinType.LEFT);
        Join<Assignement, Employee> employee = assignement.join("employee", JoinType.LEFT);

        String pattern = "%" + query.toLowerCase() + "%";

        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);
        req.select(responsability).distinct(true);

        req.orderBy(orderType.equals("asc") ? cb.asc(responsability.get("role")) :
                cb.desc(responsability.get("role")));

        req.where(responsabilityPredicate);
        return entityManager.createQuery(req);
    }

    public Long count(String filter, String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> req = cb.createQuery(Long.class);
        Root<Responsability> responsability = req.from(Responsability.class);
        Join<Responsability, Assignement> assignement = responsability.join("assignements", JoinType.LEFT);
        Join<Assignement, Employee> employee = assignement.join("employee", JoinType.LEFT);
        Join<Assignement, Restaurant> restaurant = assignement.join("restaurant", JoinType.LEFT);

        String pattern = "%" + query.toLowerCase() + "%";

        req.select(cb.countDistinct(responsability));

        Predicate responsabilityPredicate = cb.like(cb.lower(responsability.get("role")), pattern);

        req.where(responsabilityPredicate);
        TypedQuery<Long> typedQuery = entityManager.createQuery(req);
        return typedQuery.getSingleResult();
    }

    public List<Object> mapResponsability(List<Responsability> responsabilities) {
        try {
            return responsabilities.stream().map(e -> {
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
