package com.gdu.wacdo.templateRouter;

import com.gdu.wacdo.model.*;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class routeController {

    @Autowired
    private RestaurantService restaurantService;


    @GetMapping("/employee")
    public String employeeView(Model model) {
        Employee e = new Employee();
        model.addAttribute("employee", e);  // <-- nécessaire pour th:object
        return "employee"; // correspond à home.html
    }

    @GetMapping("/responsability")
    public String responsabilityView(Model model) {
        Responsability r = new Responsability();
        model.addAttribute("responsability", r);  // <-- nécessaire pour th:object
        return "responsability"; // correspond à home.html
    }

    @GetMapping("/role")
    public String roleView(Model model) {
        Role r = new Role();
        model.addAttribute("role", r);  // <-- nécessaire pour th:object
        return "role"; // correspond à home.html
    }

    @GetMapping("/restaurant")
    public String restaurantView(Model model) {
        Restaurant r = new Restaurant();
        r.setRestaurantAddress(new RestaurantAddress()); // très important si tu as des champs imbriqués
        model.addAttribute("restaurant", r);  // <-- nécessaire pour th:object
        return "restaurant"; // correspond à home.html
    }

    @GetMapping("/modalDetails")
    public String modalDetails() {
        return "fragments/modals/modalDetails :: modalDetails";
    }

    @GetMapping("/modalRestaurantEdit")
    public String modalRestaurantEdit(Model model) {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantAddress(new RestaurantAddress()); // ⚠️ obligatoire
        model.addAttribute("restaurant", restaurant);
        return "fragments/modals/modalRestaurantEdit :: modalRestaurantEdit";
    }

    @GetMapping("/modalEmployeeEdit")
    public String modalEmployeeEdit(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "fragments/modals/modalEmployeeEdit :: modalEmployeeEdit";
    }
    @GetMapping("/navbar")
    public String navbar() {
        return "fragments/navbar :: navbar";
    }

}
