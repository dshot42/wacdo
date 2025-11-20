package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> search(@RequestParam String filter, @RequestParam String query, @RequestParam int limit, @RequestParam int offset) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.ok(restaurantService.getAll(limit, offset));
            }
            return ResponseEntity.ok(restaurantService.find(filter, query, limit, offset));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String filter, @RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return restaurantService.countAll();
        }
        return restaurantService.count(filter, query);
    }

    @PostMapping("/save")
    public String saveRestaurant(@ModelAttribute Restaurant restaurant, Model model) {
        restaurantService.save(restaurant);
        model.addAttribute("restaurant", restaurant);
        return "home :: restaurant"; // <-- Thymeleaf fragment
    }

}
