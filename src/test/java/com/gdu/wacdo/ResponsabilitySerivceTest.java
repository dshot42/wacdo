package com.gdu.wacdo;


import com.gdu.wacdo.model.*;
import com.gdu.wacdo.service.ResponsabilityService;
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
import static org.assertj.core.api.Assertions.assertThatCharSequence;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class ResponsabilitySerivceTest {

    @Autowired
    Environment env;

    @Test
    void checkDb() {
        System.out.println(env.getProperty("spring.datasource.url"));
    }

    @Autowired
    MockMvc mockMvc;


    @Autowired
    ResponsabilityService responsabilityService;

    @Test
    void insertResponsability() throws Exception {

        Responsability res = new Responsability();
        res.setRole("barman");
        responsabilityService.repository.save(res);

       assertThatCharSequence("barman").isEqualTo(res.getRole());
    }

    @Test
    void findResponsability() throws Exception {
        Responsability res = new Responsability();
        res.setRole("barman");
        responsabilityService.repository.save(res);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/responsability/search")
                                .param("filter", "role")
                                .param("query",res.getRole())
                                .param("limit", "10")
                                .param("offset", "0")
                                .param("order", "asc")

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void countResponsability() throws Exception {
        Responsability res = new Responsability();
        res.setRole("barman");
        responsabilityService.repository.save(res);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/responsability/count")
                                .param("filter", "role")
                                .param("query",res.getRole())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}