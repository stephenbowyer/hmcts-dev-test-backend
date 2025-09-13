package uk.gov.hmcts.reform.dev.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import uk.gov.hmcts.reform.dev.models.TaskModel;
import uk.gov.hmcts.reform.dev.repositories.TaskRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
class TaskTest {

    @MockitoBean private TaskRepository taskRepository;
    @Autowired private transient MockMvc mockMvc;

    @DisplayName("Should add new record with 201 response code")

    @Test
    void postTaskTest() throws Exception {
        String ranNum = String.valueOf(Math.random());
        TaskModel mockTask = new TaskModel(1,
            "Test Event Title " + ranNum,
            "Event Description " + ranNum,
            "Event Status " + ranNum,
            LocalDate.now());
        when(taskRepository.save(any(TaskModel.class))).thenReturn(mockTask);
        final ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
        final String jsonContent = mapper.writeValueAsString(mockTask);
        MvcResult response = mockMvc.perform(post("/api/task")
            .accept("application/json")
            .content(jsonContent)
            .contentType("application/json"))
            .andExpect(status().isCreated())
            .andReturn();
        
        TaskModel responseTask = mapper.readValue(response.getResponse().getContentAsString(), TaskModel.class);
        assertThat(responseTask.getTitle()).isEqualTo(mockTask.getTitle());
        assertThat(responseTask.getDescription()).isEqualTo(mockTask.getDescription());
        assertThat(responseTask.getTitle()).isEqualTo(mockTask.getTitle());
        assertThat(responseTask.getDueDate()).isEqualTo(mockTask.getDueDate());
    }

    @DisplayName("Should add new record with 201 response code if no description given")

    @Test
    void postTaskWithoutDescTest() throws Exception {
        String ranNum = String.valueOf(Math.random());
        TaskModel mockTask = new TaskModel(1,
            "Test Event Title " + ranNum,
            null,
            "Event Status " + ranNum,
            LocalDate.now());
        when(taskRepository.save(any(TaskModel.class))).thenReturn(mockTask);
        final ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
        final String jsonContent = mapper.writeValueAsString(mockTask);
        MvcResult response = mockMvc.perform(post("/api/task")
            .accept("application/json")
            .content(jsonContent)
            .contentType("application/json"))
            .andExpect(status().isCreated())
            .andReturn();
        
        TaskModel responseTask = mapper.readValue(response.getResponse().getContentAsString(), TaskModel.class);
        assertThat(responseTask.getTitle()).isEqualTo(mockTask.getTitle());
        assertThat(responseTask.getDescription()).isNullOrEmpty();
        assertThat(responseTask.getTitle()).isEqualTo(mockTask.getTitle());
        assertThat(responseTask.getDueDate()).isEqualTo(mockTask.getDueDate());
    }

    @DisplayName("Should return error 400 when submitting task without title")

    @Test
    void postTaskWithoutTitleFailTest() throws Exception {
        String ranNum = String.valueOf(Math.random());
        TaskModel mockTask = new TaskModel(2,
            null,
            "Event Description " + ranNum,
            "Event Status " + ranNum,
            LocalDate.now());
        when(taskRepository.save(any(TaskModel.class))).thenReturn(mockTask);
        final ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
        final String jsonContent = mapper.writeValueAsString(mockTask);
        MvcResult response = mockMvc.perform(post("/api/task")
            .accept("application/json")
            .content(jsonContent)
            .contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(400);
    }

    @DisplayName("Should return error 400 when submitting task without status")

    @Test
    void postTaskWithoutStatusFailTest() throws Exception {
        String ranNum = String.valueOf(Math.random());
        TaskModel mockTask = new TaskModel(2,
            "Event Title " + ranNum,
            "Event Description " + ranNum,
            null,
            LocalDate.now());
        when(taskRepository.save(any(TaskModel.class))).thenReturn(mockTask);
        final ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
        final String jsonContent = mapper.writeValueAsString(mockTask);
        MvcResult response = mockMvc.perform(post("/api/task")
            .accept("application/json")
            .content(jsonContent)
            .contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(400);
    }

    @DisplayName("Should return error 400 when submitting task without duedate")

    @Test
    void postTaskWithoutDueDateFailTest() throws Exception {
        String ranNum = String.valueOf(Math.random());
        TaskModel mockTask = new TaskModel(2,
            "Event Title " + ranNum,
            "Event Description " + ranNum,
            "Event Status " + ranNum,
            null);
        when(taskRepository.save(any(TaskModel.class))).thenReturn(mockTask);
        final ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
        final String jsonContent = mapper.writeValueAsString(mockTask);
        MvcResult response = mockMvc.perform(post("/api/task")
            .accept("application/json")
            .content(jsonContent)
            .contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(400);
    }

    @DisplayName("Should return 200 when task found")

    @Test
    void getTaskByIdTest() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/task/1")
            .accept("application/json"))
            .andExpect(status().isNotFound())
            .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(404);
    }

    @DisplayName("Should return 404 when task not found")

    @Test
    void getTaskByIdFailTest() throws Exception {
        MvcResult response = mockMvc.perform(get("/api/task/99999")
            .accept("application/json"))
            .andExpect(status().isNotFound())
            .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(404);
    }

    @DisplayName("Should return array of all tasks")

    @Test
    void getAllTasksTest() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        MvcResult response = mockMvc.perform(get("/api/task")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(200);
        assertThat(response.getResponse().getContentAsString()).isNotEmpty();
        JsonNode jsonNode = mapper.readTree(response.getResponse().getContentAsString());
        assertThat(jsonNode.isArray()).isTrue();
    }

}