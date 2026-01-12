package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Assignement;
import com.gdu.wacdo.service.AssignementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignement")
public class AssignementController {

    @Autowired
    private AssignementService assignementService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/getAll")
    @ResponseBody
    public List<Assignement> listAssignements(Model model) {
        return assignementService.getAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Assignement> search(@RequestParam String filter, @RequestParam String query, @RequestParam int limit, @RequestParam int offset) {
        if (query == null || query.trim().isEmpty()) {
            return assignementService.getAll(limit, offset);
        }
        return assignementService.find(filter, query, limit, offset);
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String filter, @RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return assignementService.countAll();
        }
        return assignementService.count(filter, query);
    }

    @Transactional
    @PostMapping("/save")
    public String saveAssignement(@ModelAttribute Assignement assignement, Model model) {
        try {
            assignementService.save(assignement);
            return "[SUCCESS] persit assignement"; // <-- Thymeleaf fragment
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @PostMapping("/delete")
    public String deleteAssignement(
            @RequestParam Long employeeId,
            @RequestParam Long restaurantId
    ) {

        Assignement ass = assignementService.repository
                .findByRestaurantIdAndEmployeeId(restaurantId, employeeId)
                .orElse(null);

        if (ass == null) {
            return "[ERROR] assignement not found";
        }

        assignementService.repository.delete(ass);
        return "[SUCCESS] remove assignement";
    }


}
