package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/getAll")
    @ResponseBody
    public List<Employee> listEmployees(Model model) {
        return employeeService.getAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Object> search(@RequestParam String filter, @RequestParam String query, @RequestParam boolean withoutAssignement, @RequestParam String order, @RequestParam int limit, @RequestParam int offset) {
        return employeeService.find(filter, query,withoutAssignement, limit, offset, order);
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String filter, @RequestParam String query, @RequestParam boolean withoutAssignement) {
        return employeeService.count(filter, query,withoutAssignement);
    }

    @Transactional
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee, Model model) {
        Employee dbEmployee = employeeService.repository.findById(employee.getId())
                .orElse(null);

        if (dbEmployee == null ) {
            employeeService.saveEmployee(employee);
            return "[SUCCESS] new Employee saved"; // <-- Thymeleaf fragment
        }

        // 🔐 password non fourni → on garde l'ancien
        if (employee.getPassword() == null) {
            employee.setPassword(dbEmployee.getPassword());
        } else {
            employee.setPassword(
                    passwordEncoder.encode(employee.getPassword())
            );
        }

        model.addAttribute("employee", employee);
        return "[SUCCESS] update Employee saved";
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
