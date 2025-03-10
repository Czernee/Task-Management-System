package com.example.task_management_system;

import com.example.task_management_system.controllers.CommentController;
import com.example.task_management_system.dto.CommentDto;
import com.example.task_management_system.dto.CommentResponse;
import com.example.task_management_system.dto.TaskDto;
import com.example.task_management_system.models.Comment;
import com.example.task_management_system.models.Task;
import com.example.task_management_system.models.TaskPriority;
import com.example.task_management_system.models.TaskStatus;
import com.example.task_management_system.service.CommentService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentDto commentDto;

    @BeforeEach
    public void init() {
        commentDto = CommentDto.builder()
                .text("new comment")
                .createdDate(LocalDateTime.now())
                .updatedDate(null)
                .build();
    }

    @Test
    public void CommentController_CreateComment_ReturnCreated() throws Exception {
        given(commentService.createComment(ArgumentMatchers.anyInt(), ArgumentMatchers.any())).willReturn(commentDto);


        ResultActions response = mockMvc.perform(post("/api/tasks/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", CoreMatchers.is(commentDto.getText())));
    }

    @Test
    public void CommentController_CommentDetail_ReturnCommentDto() throws Exception {
        when(commentService.getCommentById(1, 1)).thenReturn(commentDto);

        ResultActions response = mockMvc.perform(get("/api/tasks/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(commentDto.getText())));

    }

    @Test
    public void CommentController_DeleteComment_ReturnString() throws Exception {
        int taskId = 1;
        int commentId = 1;
        doNothing().when(commentService).deleteComment(taskId, commentId);

        ResultActions response = mockMvc.perform(delete("/api/tasks/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}

