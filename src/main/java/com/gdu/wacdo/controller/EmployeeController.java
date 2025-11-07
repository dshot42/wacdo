package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.service.EmployeeService;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/getAll")
    @ResponseBody
    public List<Employee> listRestaurants(Model model) {
        return employeeService.getAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Employee> search(@RequestParam String query, @RequestParam int limit, @RequestParam int offset) {
        if (query == null || query.trim().isEmpty()) {
            return employeeService.getAll(limit, offset);
        }
        return employeeService.find(query, limit, offset);
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return employeeService.countAll();
        }
        return employeeService.count(query);
    }

    @PostMapping("/save")
    public String saveRestaurant(@ModelAttribute Employee employee, Model model) {
        employeeService.save(employee);
        model.addAttribute("employee", employee);
        return "home :: employee"; // <-- Thymeleaf fragment
    }
}
