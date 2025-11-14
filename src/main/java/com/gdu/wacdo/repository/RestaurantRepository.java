package com.gdu.wacdo.repository;

import com.gdu.wacdo.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Restaurant.class, idClass = Long.class)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
