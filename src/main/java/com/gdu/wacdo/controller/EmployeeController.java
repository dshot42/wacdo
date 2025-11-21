package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Employee> listEmployees(Model model) {
        return employeeService.getAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Object> search(@RequestParam String filter, @RequestParam String query, @RequestParam String order, @RequestParam int limit, @RequestParam int offset) {
        if (query == null || query.trim().isEmpty()) {
            return employeeService.getAll(limit, offset);
        }
        return employeeService.findByRestaurant(filter, query, limit, offset, order);
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String filter, @RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return employeeService.countAll();
        }
        return employeeService.count(filter, query);
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee, Model model) {
        employeeService.saveEmployee(employee);
        model.addAttribute("employee", employee);
        return "home :: employee"; // <-- Thymeleaf fragment
    }


    @GetMapping("/search/restaurant-id/{idRestaurant}")
    @ResponseBody
    public ResponseEntity<?> findEmployeeByRestaurant(@RequestParam String filter, @RequestParam String query, @RequestParam String order, @PathVariable Long idRestaurant) {
        try {
            return ResponseEntity.ok(employeeService.findByRestaurant(filter, query, order, idRestaurant));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
