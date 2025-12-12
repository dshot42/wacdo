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
    private String query;


    @GetMapping("/getAll")
    @ResponseBody
    public List<Restaurant> listRestaurants(Model model) {
        return restaurantService.getAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam String filter, @RequestParam String query, @RequestParam boolean withoutAssignement,  @RequestParam int limit, @RequestParam int offset) {
        try {
            return ResponseEntity.ok(restaurantService.find(filter, query, withoutAssignement, limit, offset,"asc"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String filter, @RequestParam String query,@RequestParam boolean withoutAssignement ) {
        return restaurantService.count(filter, query,withoutAssignement);
    }

    @PostMapping("/save")
    public String saveRestaurant(@ModelAttribute Restaurant restaurant, Model model) {
        restaurantService.save(restaurant);
        model.addAttribute("restaurant", restaurant);
        return "home :: restaurant"; // <-- Thymeleaf fragment
    }

    @GetMapping("/search/employee-id/{idEmployee}")
    @ResponseBody
    public ResponseEntity<?> findEmployeeByRestaurant(@RequestParam String filter, @RequestParam String query, @RequestParam String order, @PathVariable Long idEmployee) {
        this.query = query;
        try {
            return ResponseEntity.ok(restaurantService.findByEmployee(filter, query, order, idEmployee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
