package com.springboot.Spring_B.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for {@link NewStudentController} ensuring exception handling paths.
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
        Student student = new Student("Jane", "jane@example.com", LocalDate.now(), Collections.emptyList());
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @Test
    void addBookNotFoundThrowsException() throws Exception {
        when(studentService.addBook(eq(1L), any(Book.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/students/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentByIdNotFoundReturns404() throws Exception {
        when(studentService.deleteStudentById(9L)).thenReturn(false);

        mockMvc.perform(delete("/students/9"))
                .andExpect(status().isNotFound());
    }
}
