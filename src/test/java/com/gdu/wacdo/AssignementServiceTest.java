package com.gdu.wacdo;


import com.gdu.wacdo.model.*;
import com.gdu.wacdo.service.AssignementService;
import com.gdu.wacdo.service.EmployeeService;
import com.gdu.wacdo.service.ResponsabilityService;
import com.gdu.wacdo.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class AssignementServiceTest {

    @Autowired
    Environment env;

    @Test
    void checkDb() {
        System.out.println(env.getProperty("spring.datasource.url"));
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    AssignementService assignementService;

    @Autowired
    ResponsabilityService responsabilityService;

    @Test
    void insertAssignement() throws Exception {
        Employee e = new Employee();
        e.setName("Employee test");
        e.setSurname("Employee test");
        e.setMail("test@test.fr");
        e.setHireDate(LocalDate.now());
        e.setPhone("0601010101");
        e.setPassword(new BCryptPasswordEncoder().encode("1234"));
        employeeService.saveEmployee(e);

        Restaurant r = new Restaurant();
        r.setName("Restaurant test");
        restaurantService.save(r);

        Responsability res = new Responsability();
        res.setRole("barman");
        responsabilityService.repository.save(res);

        Assignement ass = new Assignement();
        ass.setId(new AssignementId(e.getId(), r.getId()));

        ass.setEmployee(e);
        ass.setRestaurant(r);
        ass.setResponsability(res);
        ass.setStartDate(LocalDate.now());
        assignementService.save(ass);
        assignementService.repository.flush();

        assertThat(assignementService.repository.findAll()).hasSize(1);
    }

    @Test
    void findAssignement() throws Exception {
        Employee e = new Employee();
        e.setName("Employee test");
        e.setSurname("Employee test");
        e.setMail("test@test.fr");
        e.setHireDate(LocalDate.now());
        e.setPhone("0601010101");
        e.setPassword(new BCryptPasswordEncoder().encode("1234"));
        employeeService.saveEmployee(e);

        Restaurant r = new Restaurant();
        r.setName("Restaurant test");
        restaurantService.save(r);

        Responsability res = new Responsability();
        res.setRole("barman");

        responsabilityService.repository.save(res);

        Assignement ass = new Assignement();
        ass.setId(new AssignementId(e.getId(), r.getId()));

        ass.setEmployee(e);
        ass.setRestaurant(r);
        ass.setResponsability(res);
        ass.setStartDate(LocalDate.now());
        assignementService.save(ass);
        assignementService.repository.flush();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/assignement/search")
                                .param("filter", "restaurant_name")
                                .param("query", r.getName())
                                .param("limit", "10")
                                .param("offset", "0")

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void countAssignement() throws Exception {
        Employee e = new Employee();
        e.setName("Employee test");
        e.setSurname("Employee test");
        e.setMail("test@test.fr");
        e.setHireDate(LocalDate.now());
        e.setPhone("0601010101");
        e.setPassword(new BCryptPasswordEncoder().encode("1234"));
        employeeService.saveEmployee(e);

        Restaurant r = new Restaurant();
        r.setName("Restaurant test");
        restaurantService.save(r);

        Responsability res = new Responsability();
        res.setRole("barman");
        responsabilityService.repository.save(res);

        Assignement ass = new Assignement();
        ass.setId(new AssignementId(e.getId(), r.getId()));

        ass.setEmployee(e);
        ass.setRestaurant(r);
        ass.setResponsability(res);
        ass.setStartDate(LocalDate.now());
        assignementService.save(ass);
        assignementService.repository.flush();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/assignement/count")
                                .param("filter", "employee_name")
                                .param("query", e.getName())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}