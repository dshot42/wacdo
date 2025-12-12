package com.gdu.wacdo.test.service;

import com.gdu.wacdo.model.*;
import com.gdu.wacdo.repository.AssignementRepository;
import com.gdu.wacdo.repository.EmployeeRepository;
import com.gdu.wacdo.repository.ResponsabilityRepository;
import com.gdu.wacdo.repository.RoleRepository;
import com.gdu.wacdo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class InsertTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ResponsabilityRepository responsabilityRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AssignementRepository assignementRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void deleteAll() {
        employeeRepository.deleteAll();
        restaurantService.repository.deleteAll();
    }

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
                //   List.of("Le Comptoir du Relais", "9 Carrefour de l’Odéon", "75006", "Paris", 48.8515, 2.3396),
                // Lyon
                List.of("La Mère Brazier", "12 Rue Royale", "69001", "Lyon", 45.7686, 4.8352),
                List.of("Le Cintra", "43 Rue de la Bourse", "69002", "Lyon", 45.7614, 4.8359),
                List.of("Jour de Marché (Krak)", "14 Rue Molière", "69006", "Lyon", 45.7660, 4.8491),
                List.of("Addis Abeba", "264 Rue Duguesclin", "69003", "Lyon", 45.7559, 4.8547),
                List.of("Le Kitchen Café", "34 Rue Chevreul", "69007", "Lyon", 45.7472, 4.8416),
                List.of("Le Bouchon des Filles", "20 Rue Sergent Blandan", "69001", "Lyon", 45.7672, 4.8331),
                List.of("Le Bouchon Tupin", "15 Rue Tupin", "69002", "Lyon", 45.7596, 4.8324),
                List.of("Le Bistrot du Potager", "3 Rue de la Martinière", "69001", "Lyon", 45.7679, 4.8294),
                List.of("Le Café du Peintre", "50 Rue de Sèze", "69006", "Lyon", 45.7683, 4.8502)
                //  List.of("Le Gourmet de Sèze", "129 Rue de Sèze", "69006", "Lyon", 45.7701, 4.8527)
        );

        restaurants.forEach(e -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setName(e.get(0).toString());
            RestaurantAddress address = new RestaurantAddress();
            address.setAddress(e.get(1).toString());
            address.setPostalCode(e.get(2).toString());
            address.setCity(e.get(3).toString());
            address.setCordX(Float.parseFloat(e.get(4).toString()));
            address.setCordY(Float.parseFloat(e.get(5).toString()));

            restaurant.setRestaurantAddress(address);
            restaurantService.save(restaurant);
        });

    }

    public void insertResponsability() {
        List<String> metiers = new ArrayList<>();
        metiers.add("Gérant de restaurant");
        metiers.add("Chef cuisinier");
        metiers.add("Serveur");
        metiers.add("Barman");
        metiers.add("Plongeur");

        metiers.forEach(e -> {
            Responsability res = new Responsability();
            res.setRole(e);
            responsabilityRepository.save(res);
        });
    }

    public void insertRole() {
        List<String> roles = new ArrayList<>();

        roles.add("ADMINISTRATOR");
        roles.add("RESPONSABLE");
        roles.add("EMPLOYEE");

        roles.forEach(e -> {
            Role role = new Role();
            role.setRoleName(e);
            roleRepository.save(role);
        });
    }

    public void insertEmployee() {
        List<Role> roles = roleRepository.findAll();
        List<Responsability> responsabilities = responsabilityRepository.findAll();

        List<List<Object>> employees = List.of(
                List.of("Jean", "Dupont", LocalDate.of(2021, 3, 12), "jean.dupont@mail.com", "0600000001", roles.get(0), responsabilities.get(4)),
                List.of("Marie", "Durand", LocalDate.of(2020, 7, 22), "marie.durand@mail.com", "0600000002", roles.get(1), responsabilities.get(4)),
                List.of("Paul", "Martin", LocalDate.of(2019, 5, 18), "paul.martin@mail.com", "0600000003", roles.get(2), responsabilities.get(0)),
                List.of("Sophie", "Bernard", LocalDate.of(2022, 1, 9), "sophie.bernard@mail.com", "0600000004", roles.get(2), responsabilities.get(0)),
                List.of("Luc", "Petit", LocalDate.of(2023, 2, 14), "luc.petit@mail.com", "0600000005", roles.get(2), responsabilities.get(0)),
                List.of("Emma", "Robert", LocalDate.of(2020, 11, 3), "emma.robert@mail.com", "0600000006", roles.get(1), responsabilities.get(4)),
                List.of("Antoine", "Moreau", LocalDate.of(2021, 6, 10), "antoine.moreau@mail.com", "0600000007", roles.get(2), responsabilities.get(0)),
                List.of("Camille", "Laurent", LocalDate.of(2019, 9, 25), "camille.laurent@mail.com", "0600000008", roles.get(2), responsabilities.get(0)),
                List.of("Nicolas", "Garcia", LocalDate.of(2022, 12, 4), "nicolas.garcia@mail.com", "0600000009", roles.get(2), responsabilities.get(0)),
                List.of("Laura", "Fournier", LocalDate.of(2023, 3, 30), "laura.fournier@mail.com", "0600000010", roles.get(2), responsabilities.get(0)),
                List.of("Alexandre", "Girard", LocalDate.of(2021, 8, 21), "alexandre.girard@mail.com", "0600000011", roles.get(2), responsabilities.get(0)),
                List.of("Julie", "Roux", LocalDate.of(2020, 4, 6), "julie.roux@mail.com", "0600000012", roles.get(1), responsabilities.get(4)),
                List.of("Hugo", "Fontaine", LocalDate.of(2022, 10, 11), "hugo.fontaine@mail.com", "0600000013", roles.get(2), responsabilities.get(0)),
                List.of("Clara", "Chevalier", LocalDate.of(2021, 1, 28), "clara.chevalier@mail.com", "0600000014", roles.get(2), responsabilities.get(0)),
                List.of("Maxime", "Lopez", LocalDate.of(2019, 11, 7), "maxime.lopez@mail.com", "0600000015", roles.get(2), responsabilities.get(0)),
                List.of("Alice", "Renard", LocalDate.of(2020, 5, 13), "alice.renard@mail.com", "0600000016", roles.get(1), responsabilities.get(4)),
                List.of("Thomas", "Dupuis", LocalDate.of(2023, 7, 19), "thomas.dupuis@mail.com", "0600000017", roles.get(2), responsabilities.get(0)),
                List.of("Inès", "Blanc", LocalDate.of(2021, 9, 23), "ines.blanc@mail.com", "0600000018", roles.get(2), responsabilities.get(0)),
                List.of("Adrien", "Legrand", LocalDate.of(2022, 2, 8), "adrien.legrand@mail.com", "0600000019", roles.get(2), responsabilities.get(0)),
                List.of("Manon", "Perrin", LocalDate.of(2020, 6, 2), "manon.perrin@mail.com", "0600000020", roles.get(2), responsabilities.get(0)),
                List.of("Romain", "Meyer", LocalDate.of(2019, 8, 5), "romain.meyer@mail.com", "0600000021", roles.get(2), responsabilities.get(0)),
                List.of("Chloé", "Marchand", LocalDate.of(2021, 12, 17), "chloe.marchand@mail.com", "0600000022", roles.get(1), responsabilities.get(4)),
                List.of("Benjamin", "Baron", LocalDate.of(2022, 4, 1), "benjamin.baron@mail.com", "0600000023", roles.get(2), responsabilities.get(0)),
                List.of("Eva", "Lemoine", LocalDate.of(2023, 10, 12), "eva.lemoine@mail.com", "0600000024", roles.get(2), responsabilities.get(0)),
                List.of("Lucas", "Gaillard", LocalDate.of(2020, 1, 26), "lucas.gaillard@mail.com", "0600000025", roles.get(2), responsabilities.get(0))
        );


        employees.forEach(e -> {
            Employee empl = new Employee();
            empl.setSurname((String) e.get(0));
            empl.setName((String) e.get(1));
            empl.setHireDate((LocalDate) e.get(2));
            empl.setMail((String) e.get(3));
            empl.setPhone((String) e.get(4));
            empl.setRole((Role) e.get(5));
            String encodedPassword = passwordEncoder.encode("123");
            empl.setPassword(encodedPassword);
            employeeRepository.save(empl);
        });
    }

    // assignement employee restau  + drag drop

    public void assignementEmployeeRestaurant() throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        List<Restaurant> restaurants = restaurantService.getAll();
        restaurants.remove(5); // pour avoir des restaurant without asssignements
        restaurants.remove(8);
        List<Responsability> responsabilitys = responsabilityRepository.findAll();
        Random rand = new Random();

        int i = 0;
        for (Restaurant r : restaurants) {
            for (int j = 0; j <= 4; j++) { // 4 empl par restau
                try {
                    Assignement assignement = new Assignement();
                    Employee randomEmployee = employees.get(rand.nextInt(employees.size() - 1));
                    assignement.setEmployee(randomEmployee);
                    assignement.setRestaurant(restaurants.get(i));
                    assignement.setStartDate(LocalDate.now());

                    Responsability responsability = responsabilitys.get(j);
                    assignement.setId(new AssignementId(randomEmployee.getId(), r.getId()));
                    assignement.setResponsability(responsability);
                    assignementRepository.save(assignement);
                } catch (Exception ex) {
                    throw new Exception("No more restaurant available ", ex);
                }
            }
            i++;

        }
    }

}
