package com.example.task_management_system.controllers;

import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.models.Task;
import com.example.task_management_system.models.TaskPriority;
import com.example.task_management_system.models.TaskStatus;
import com.example.task_management_system.models.UserEntity;
import com.example.task_management_system.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDto taskDto;

    @BeforeEach
    public void init() {
        taskDto = TaskDto.builder()
                .title("title")
                .description("description")
                .status(TaskStatus.valueOf("IN_WAIT"))
                .priority(TaskPriority.valueOf("MEDIUM"))
                .build();
    }

    @Test
    public void TaskController_CreateTask_ReturnCreated() throws Exception {
        given(taskService.createTask(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(1));

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)));

        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(taskDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(taskDto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(taskDto.getStatus().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority", CoreMatchers.is(taskDto.getPriority().toString())));
    }

    @Test
    public void TaskController_CreateInvalidDataTask_ReturnBadRequest() throws Exception {
        TaskDto invalidTaskDto = new TaskDto();
        invalidTaskDto.setTitle("");
        invalidTaskDto.setDescription("Test description");
        invalidTaskDto.setStatus(TaskStatus.IN_WAIT);
        invalidTaskDto.setPriority(TaskPriority.LOW);

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTaskDto)));

        response.andExpect(status().isBadRequest());
    }
}
