package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public List<Object> search(@RequestParam String filter, @RequestParam String query, @RequestParam int limit, @RequestParam int offset) {
        if (query == null || query.trim().isEmpty()) {
            return employeeService.getAll(limit, offset);
        }
        return employeeService.find(filter, query, limit, offset);
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
        if (employee.getId() == null) {
            employeeService.repository.findByMail(employee.getMail())
                    .ifPresent(
                            e -> employee.setId(e.getId())
                    );
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeService.save(employee);
        model.addAttribute("employee", employee);
        return "home :: employee"; // <-- Thymeleaf fragment
    }

}
