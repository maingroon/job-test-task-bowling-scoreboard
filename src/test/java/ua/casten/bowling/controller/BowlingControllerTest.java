package ua.casten.bowling.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ua.casten.bowling.service.BowlingService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BowlingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BowlingService bowlingService;

    @Test
    void contextLoads() {
        assertNotNull(bowlingService);
        assertNotNull(mockMvc);
    }

    @Test
    void testGetBowlingPage() throws Exception {
        mockMvc.perform(get("/bowling"))
                .andExpect(status().isOk());
    }

}
