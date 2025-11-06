package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.model.RestaurantAddress;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    @GetMapping("/getAll")
    @ResponseBody
    public List<Restaurant> listRestaurants(Model model) {
        return restaurantService.getAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Restaurant> search(@RequestParam String query, @RequestParam int limit, @RequestParam int offset) {
        if (query == null || query.trim().isEmpty()) {
            return restaurantService.getAll(limit, offset);
        }
        return restaurantService.find(query, limit, offset);
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return restaurantService.countAll();
        }
        return restaurantService.count(query);
    }

    @PostMapping("/save")
    public String saveRestaurant(@ModelAttribute Restaurant restaurant, Model model) {
        restaurantService.save(restaurant);
        model.addAttribute("restaurant", restaurant);
        return "home :: restaurant"; // <-- Thymeleaf fragment
    }
}
