package com.gdu.wacdo.templateRouter;

import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.model.RestaurantAddress;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class routeController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/home")
    public String homeView(Model model) {
        Restaurant r = new Restaurant();
        r.setRestaurantAddress(new RestaurantAddress()); // très important si tu as des champs imbriqués
        model.addAttribute("restaurant", r);  // <-- nécessaire pour th:object
        return "restaurant"; // correspond à home.html
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
        return "fragments/modals/modalDetails :: modalDetails";  //return "/modals/modal-edit"; // recherche modal.html dans templates/
         //return "/modals/modal-details"; // recherche modal.html dans templates/
    }

    @GetMapping("/modalEdit")
    public String modalEdit(Model model) {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantAddress(new RestaurantAddress()); // ⚠️ obligatoire
        model.addAttribute("restaurant", restaurant);
        return "fragments/modals/modalEdit :: modalEdit";
    }

    @GetMapping("/navbar")
    public String navbar() {
        return "navbar"; // recherche modal.html dans templates/
    }


}
