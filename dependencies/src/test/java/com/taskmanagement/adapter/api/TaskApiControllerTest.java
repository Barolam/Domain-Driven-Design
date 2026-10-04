package com.taskmanagement.adapter.api;

import com.taskmanagement.infrastructure.api.ApiApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
class TaskApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/tasks with valid request should return 201 CREATED")
    void shouldCreateTaskSuccessfully() throws Exception {
        String requestBody = """
            {
                "title": "Viet Unit Test cho Controller",
                "description": "Kiem tra endpoint REST API bang MockMvc",
                "priority": "HIGH"
            }
            """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.taskId", notNullValue()))
            .andExpect(jsonPath("$.data.title", is("Viet Unit Test cho Controller")))
            .andExpect(jsonPath("$.data.statusDisplay", is("Can lam")));
    }

    @Test
    @DisplayName("POST /api/tasks with blank title should return 400 BAD REQUEST")
    void shouldReturnBadRequestWhenTitleIsBlank() throws Exception {
        String requestBody = """
            {
                "title": "   ",
                "description": "Invalid Title"
            }
            """;

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error.code", is("VALIDATION_ERROR")));
    }

    @Test
    @DisplayName("GET /api/tasks/{taskId} with non-existent UUID should return 404 NOT FOUND")
    void shouldReturnNotFoundForNonExistentTask() throws Exception {
        mockMvc.perform(get("/api/tasks/00000000-0000-0000-0000-000000000000"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error.code", is("TASK_NOT_FOUND")));
    }
}
