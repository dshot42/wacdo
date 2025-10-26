package com.gdu.wacdo.test.service;

import com.gdu.wacdo.model.Responsability;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.model.RestaurantAddress;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsertTest {

    @Autowired
    private RestaurantService restaurantService;


    public void insertRestaurant() {
        List<List<Object>> restaurants = List.of(
                // Paris
                List.of("Le Meurice Alain Ducasse", "228 Rue de Rivoli", "75001", "Paris", 48.8656, 2.3286),
                List.of("Septime", "80 Rue de Charonne", "75011", "Paris", 48.8532, 2.3807),
                List.of("Le Chateaubriand", "129 Avenue Parmentier", "75011", "Paris", 48.8660, 2.3768),
                List.of("Pierre Gagnaire", "6 Rue Balzac", "75008", "Paris", 48.8715, 2.3018),
                List.of("L’Ambroisie", "9 Place des Vosges", "75004", "Paris", 48.8555, 2.3658),
                List.of("Le Cinq (Four Seasons George V)", "31 Avenue George V", "75008", "Paris", 48.8688, 2.3003),
                List.of("Frenchie", "5 Rue du Nil", "75002", "Paris", 48.8681, 2.3491),
                List.of("L’Arpège", "84 Rue de Varenne", "75007", "Paris", 48.8534, 2.3176),
                List.of("Guy Savoy", "Monnaie de Paris, 11 Quai de Conti", "75006", "Paris", 48.8561, 2.3422),
                List.of("Le Comptoir du Relais", "9 Carrefour de l’Odéon", "75006", "Paris", 48.8515, 2.3396),
                // Lyon
                List.of("La Mère Brazier", "12 Rue Royale", "69001", "Lyon", 45.7686, 4.8352),
                List.of("Le Cintra", "43 Rue de la Bourse", "69002", "Lyon", 45.7614, 4.8359),
                List.of("Jour de Marché (Krak)", "14 Rue Molière", "69006", "Lyon", 45.7660, 4.8491),
                List.of("Addis Abeba", "264 Rue Duguesclin", "69003", "Lyon", 45.7559, 4.8547),
                List.of("Le Kitchen Café", "34 Rue Chevreul", "69007", "Lyon", 45.7472, 4.8416),
                List.of("Le Bouchon des Filles", "20 Rue Sergent Blandan", "69001", "Lyon", 45.7672, 4.8331),
                List.of("Le Bouchon Tupin", "15 Rue Tupin", "69002", "Lyon", 45.7596, 4.8324),
                List.of("Le Bistrot du Potager", "3 Rue de la Martinière", "69001", "Lyon", 45.7679, 4.8294),
                List.of("Le Café du Peintre", "50 Rue de Sèze", "69006", "Lyon", 45.7683, 4.8502),
                List.of("Le Gourmet de Sèze", "129 Rue de Sèze", "69006", "Lyon", 45.7701, 4.8527)
        );

        restaurants.forEach(e->  {
            Restaurant restaurant = new Restaurant(e.get(0).toString());
            RestaurantAddress address = new RestaurantAddress();
            address.setAddress(e.get(1).toString());
            address.setPostalCode(Integer.parseInt(e.get(2).toString()));
            address.setCity(e.get(3).toString());
            address.setCordX(Float.parseFloat(e.get(4).toString()));
            address.setCordY(Float.parseFloat(e.get(5).toString()));

            restaurant.setRestaurantAddress(address);
            restaurantService.save(restaurant);
        });

    }


}
