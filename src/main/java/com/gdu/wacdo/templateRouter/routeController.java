package com.gdu.wacdo.templateRouter;

import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class routeController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurant")
    public String restaurantView(Model model) {
        // model.addAttribute("restaurants", restaurantService.getAll());
        return "restaurant"; // correspond à home.html
    }

    @GetMapping("/modal")
    public String modal() {
        return "modal"; // recherche modal.html dans templates/
    }

    @GetMapping("/navbar")
    public String navbar() {
        return "navbar"; // recherche modal.html dans templates/
    }


}
