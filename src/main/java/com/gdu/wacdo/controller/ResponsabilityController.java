package com.gdu.wacdo.controller;

import com.gdu.wacdo.model.Responsability;
import com.gdu.wacdo.service.ResponsabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responsability")
public class ResponsabilityController {

    @Autowired
    private ResponsabilityService responsabilityService;

    @GetMapping("/getAll")
    @ResponseBody
    public List<Responsability> listResponsabilitys(Model model) {
        return responsabilityService.repository.findAll();
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Object> search(@RequestParam String filter, @RequestParam String query, @RequestParam String order, @RequestParam int limit, @RequestParam int offset) {
        return responsabilityService.find(filter, query, limit, offset, order);
    }

    @GetMapping("/count")
    @ResponseBody
    public Long count(@RequestParam String filter, @RequestParam String query) {
        return responsabilityService.count(filter, query);
    }

    @PostMapping("/save")
    public String saveResponsability(@ModelAttribute Responsability responsability, Model model) {
        responsabilityService.repository.save(responsability);
        model.addAttribute("responsability", responsability);
        return "home :: responsability"; // <-- Thymeleaf fragment
    }


}
