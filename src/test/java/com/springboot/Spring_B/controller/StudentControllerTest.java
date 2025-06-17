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
 * Integration style tests for {@link StudentController} verifying request/response behaviour.
 */
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void helloEndpointReturnsPlainText() throws Exception {
        mockMvc.perform(get("/student/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    void helloResponseEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/student/helloResponse"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Response is ok"));
    }

    @Test
    void addStudentReturnsCreatedStudent() throws Exception {
        Student student = new Student("John","john@example.com", LocalDate.now(), Collections.emptyList());
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));

        verify(studentService).addStudent(any(Student.class));
    }

    @Test
    void getStudentByIdReturnsNotFoundWhenServiceEmpty() throws Exception {
        when(studentService.findStudentById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentByIdRespondsBasedOnServiceResult() throws Exception {
        when(studentService.deleteStudentById(1L)).thenReturn(true);
        when(studentService.deleteStudentById(2L)).thenReturn(false);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student is deleted"));

        mockMvc.perform(delete("/student/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsReturnsListOfStudents() throws Exception {
        Student student = new Student("Jane","jane@example.com", LocalDate.now(), Collections.emptyList());
        when(studentService.findAllStudents()).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(student))));
    }

    @Test
    void addBookReturnsCreatedWhenStudentExists() throws Exception {
        Book book = new Book();
        when(studentService.addBook(eq(1L), any(Book.class))).thenReturn(Optional.of(book));

        mockMvc.perform(post("/student/1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());
    }
}
