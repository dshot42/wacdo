package com.gdu.wacdo;


import com.gdu.wacdo.model.Employee;
import com.gdu.wacdo.service.EmployeeService;
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
class EmployeeServiceTest {

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

    @Test
    void insertEmployee() {
        Employee r = new Employee();
        r.setName("Employee test");
        r.setSurname("Employee test");
        r.setMail("test@test.fr");
        r.setHireDate(LocalDate.now());
        r.setPhone("0601010101");
        r.setPassword(new BCryptPasswordEncoder().encode("1234"));
        employeeService.saveEmployee(r);
        Employee rDb = employeeService.getAll().get(0);
        assertThat(rDb.getName()).isEqualTo("Employee test");
    }

    @Test
    void updateEmployee() {
        Employee r = new Employee();
        r.setName("test");
        r.setSurname("Employee test");
        r.setMail("test@test.fr");
        r.setPhone("0601010101");

        r.setHireDate(LocalDate.now());

        r.setPassword(new BCryptPasswordEncoder().encode("1234"));
        String msg = employeeService.saveEmployee(r);

        // on update juste le Name
        r.setName("Employee test Updatee");
        String msgUp = employeeService.saveEmployee(r);

        // on compare les retours  succes insert != succes update
        assertThatCharSequence (msg).isNotEqualTo(msgUp);

        Employee rDb = employeeService.repository.findAll().get(0);
        // on check la base pour voir sil il est bien avec un name  "Update "
        assertThatCharSequence(rDb.getName()).isEqualTo(r.getName());
    }

    @Test
    void findEmployee() throws Exception {
        Employee r = new Employee();
        r.setName(" Employee test");
        r.setSurname("Employee test");
        r.setMail("test@test.fr");
        r.setPhone("0601010101");

        r.setHireDate(LocalDate.now());

        r.setPassword(new BCryptPasswordEncoder().encode("1234"));
        String msg = employeeService.saveEmployee(r);

        // 2️⃣ Appel HTTP (via controller)
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/employee/search")
                                .param("filter", "name")
                                .param("query", r.getName())
                                .param("withoutAssignement", "false")
                                .param("order", "asc")
                                .param("limit", "10")
                                .param("offset", "0")

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void countEmployee() throws Exception {
        Employee r = new Employee();
        r.setName(" Employee test");
        r.setSurname("Employee test");
        r.setMail("test@test.fr");
        r.setPhone("0601010101");

        r.setHireDate(LocalDate.now());

        r.setPassword(new BCryptPasswordEncoder().encode("1234"));
        String msg = employeeService.saveEmployee(r);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/employee/count")
                                .param("filter", "name")
                                .param("query", r.getName())
                                .param("withoutAssignement", "false")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}