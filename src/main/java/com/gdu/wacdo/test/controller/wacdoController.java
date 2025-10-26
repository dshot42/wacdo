package com.gdu.wacdo.test.controller;


import com.gdu.wacdo.repository.RestaurantRepository;
import com.gdu.wacdo.test.service.InsertTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("/test")
public class wacdoController {

    @Autowired
    private InsertTest insertTest;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/insertRestaurantTest")
    public String insertRestaurantTest() {
        insertTest.insertRestaurant();
        return "[SUCCESS] insert 10 restaurant in db!";
    }

    // clean db
    @GetMapping("/deleteAllRestaurants")
    public String deleteAllRestaurants() {
        restaurantRepository.deleteAll();
        return "[SUCCESS] clear all restaurant in db!";
    }


    // insert test
}
