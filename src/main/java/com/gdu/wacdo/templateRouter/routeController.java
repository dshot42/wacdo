package com.gdu.wacdo.templateRouter;

import com.gdu.wacdo.model.*;
import com.gdu.wacdo.repository.ResponsabilityRepository;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class routeController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ResponsabilityRepository responsabilityRepository;


    @GetMapping("/employee")
    public String employeeView(Model model) {
        Employee e = new Employee();
        model.addAttribute("employee", e);  // <-- nécessaire pour th:object
        model.addAttribute("responsabilities",responsabilityRepository.findAll());
        model.addAttribute("filter", Map.of("name","nom","surname","prenom","name+surname","nom et prenom","mail","email"));
        model.addAttribute("restaurantFilter",List.of("name","surname","mail"));
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
        model.addAttribute("filter",  Map.of("name","nom du restaurant","city","ville","postalCode","code postal"));
        model.addAttribute("employeeFilter",List.of("post","surname","mail"));
//poste, par nom, par date de début d'affectation.
        return "restaurant"; // correspond à home.html
    }

    @GetMapping("/searchbar")
    public String searchbar() {
        return "fragments/searchbar :: searchbar";
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
