package com.gdu.wacdo;


import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCharSequence;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class RestaurantServiceTest {

    @Autowired
    Environment env;

    @Test
    void checkDb() {
        System.out.println(env.getProperty("spring.datasource.url"));
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RestaurantService restaurantService;

    @Test
    void insertRestaurant() {
        Restaurant r = new Restaurant();
        r.setName("Restaurant test");
        restaurantService.save(r);
        Restaurant rDb = restaurantService.getAll().get(0);
        assertThat(rDb.getName()).isEqualTo("Restaurant test");
    }

    @Test
    void updateRestaurant() {
        Restaurant r = new Restaurant();
        r.setName("Restaurant test 2");
        r = restaurantService.save(r);
        r.setName("Restaurant test  2 Updatee");
        r = restaurantService.save(r);

        Restaurant rDb = restaurantService.repository.findAll().get(0);
        assertThatCharSequence(rDb.getName()).isEqualTo(r.getName());
    }

    @Test
    void findRestaurant() throws Exception {
        Restaurant r = new Restaurant();
        r.setName("Restaurant Test");
        restaurantService.save(r);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/restaurant/search")
                                .param("filter", "name")
                                .param("query", "Restaurant Test")
                                .param("withoutAssignement", "false")
                                .param("limit", "10")
                                .param("offset", "0")
                                .param("order", "asc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void countRestaurant() throws Exception {
        Restaurant r = new Restaurant();
        r.setName("Restaurant Test");
        restaurantService.save(r);

        // 2️⃣ Appel HTTP (via controller)
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/restaurant/count")
                                .param("filter", "name")
                                .param("query", "Restaurant Test")
                                .param("withoutAssignement", "false")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}