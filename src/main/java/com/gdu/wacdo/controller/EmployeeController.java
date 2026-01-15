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

import java.sql.SQLException;
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
    public ResponseEntity<?> search(@RequestParam String filter, @RequestParam String query, @RequestParam boolean withoutAssignement, @RequestParam String order, @RequestParam int limit, @RequestParam int offset) {
        try {
            return ResponseEntity.ok(employeeService.find(filter, query, withoutAssignement, limit, offset, order));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[ERROR] invalid request find Employee, " + e.getMessage());
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(@RequestParam String filter, @RequestParam String query, @RequestParam boolean withoutAssignement) {
        try {
            return ResponseEntity.ok(employeeService.count(filter, query, withoutAssignement));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[ERROR] invalid request count Employee, " + e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> saveEmployee(@ModelAttribute Employee employee, Model model) {
        try {
            return ResponseEntity.ok(employeeService.saveEmployee(employee));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("[ERROR] invalid request save Employee , " + e.getMessage());
        }
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
