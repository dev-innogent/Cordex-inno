package com.springboot.Spring_B.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link NewStudentController} endpoints and exception handlers.
 */
@WebMvcTest(NewStudentController.class)
class NewStudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void addStudentReturnsOk() throws Exception {
        Student student = new Student("John", "john@example.com", LocalDate.now(), Collections.emptyList());
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @Test
    void findAllStudentsReturnsList() throws Exception {
        when(studentService.findAllStudents()).thenReturn(List.of());

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getBooksReturnsList() throws Exception {
        when(studentService.findAllBooks()).thenReturn(List.of());

        mockMvc.perform(get("/students/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void findStudentByIdNotFound() throws Exception {
        when(studentService.findStudentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBookByStudentIdAndIdNotFound() throws Exception {
        when(studentService.deleteBookByStudentIdAndId(1L, 2L)).thenReturn(false);

        mockMvc.perform(delete("/students/1/books/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void handleHttpMessageNotReadableExceptionReturnsBadRequest() {
        NewStudentController controller = new NewStudentController(studentService);
        var response = controller.handleHttpMessageNotReadableException(new HttpMessageNotReadableException("bad"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleResponseStatusExceptionReturnsMessage() {
        NewStudentController controller = new NewStudentController(studentService);
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "missing");
        var response = controller.handleResponseStatusException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
