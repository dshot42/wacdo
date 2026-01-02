package com.gdu.wacdo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Set;


@SpringBootTest
@AutoConfigureMockMvc
class WacdoApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RequestMappingHandlerMapping mapping;

    @Test
    void contextLoads() {
    }

    @Test
    void hitAllGetRoutes() {
        mapping.getHandlerMethods().forEach((info, method) -> {

            Set<String> patterns;
            if (info.getPathPatternsCondition() != null) {
                patterns = info.getPathPatternsCondition().getPatternValues();
            } else {
                Assertions.assertNotNull(info.getPatternsCondition());
                patterns = info.getPatternsCondition().getPatterns();
            }

            patterns.forEach(pattern -> {
                try {
                    mockMvc.perform(
                            MockMvcRequestBuilders.get(pattern)
                    );
                } catch (Exception ignored) {
                }
            });
        });
    }
}
