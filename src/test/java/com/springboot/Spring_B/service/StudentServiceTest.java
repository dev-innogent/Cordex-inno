package com.springboot.Spring_B.service;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.repository.BookRepository;
import com.springboot.Spring_B.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StudentService} covering various edge cases.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private StudentService service;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("John", "john@example.com", LocalDate.now(), new ArrayList<>());
    }

    @Test
    void addStudentInitializesBooksListAndPersists() {
        student.setBooks(null);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = service.addStudent(student);

        assertNotNull(saved.getBooks(), "Books list should be initialized");
        verify(studentRepository).save(student);
    }

    @Test
    void addBookReturnsEmptyWhenStudentMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = service.addBook(1L, new Book());

        assertTrue(result.isEmpty(), "Result should be empty if student not found");
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addBookLinksStudentAndSaves() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Book book = new Book();

        Optional<Book> result = service.addBook(1L, book);

        assertTrue(result.isPresent(), "Book should be returned when save succeeds");
        assertSame(student, book.getStudent(), "Book should reference its student");
        verify(studentRepository).findById(1L);
        verify(bookRepository).save(book);
    }

    @Test
    void deleteStudentByIdRemovesBooksAndStudent() {
        when(studentRepository.existsById(5L)).thenReturn(true);

        boolean deleted = service.deleteStudentById(5L);

        assertTrue(deleted);
        verify(bookRepository).deleteByStudentId(5L);
        verify(studentRepository).deleteById(5L);
    }

    @Test
    void deleteStudentByIdReturnsFalseWhenAbsent() {
        when(studentRepository.existsById(5L)).thenReturn(false);

        assertFalse(service.deleteStudentById(5L));
        verify(bookRepository, never()).deleteByStudentId(anyLong());
        verify(studentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteBookByStudentIdAndIdDeletesWhenBothExist() {
        Book book = new Book();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));

        assertTrue(service.deleteBookByStudentIdAndId(1L, 2L));
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBookByStudentIdAndIdReturnsFalseWhenMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(service.deleteBookByStudentIdAndId(1L, 2L));
        verify(bookRepository, never()).delete(any());
    }

    @Test
    void updateStudentByIdUpdatesFieldsConditionally() {
        Student existing = new Student("Old", "old@example.com", LocalDate.now().minusDays(1), new ArrayList<>());
        when(studentRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student patch = new Student("New", null, null, null);
        Optional<Student> result = service.updateStudentById(3L, patch);

        assertTrue(result.isPresent());
        assertEquals("New", existing.getName());
        assertEquals("old@example.com", existing.getEmail(), "Email should remain unchanged");
        verify(studentRepository).save(existing);
    }

    @Test
    void updateBookByIdChangesValues() {
        Book existing = new Book();
        existing.setAuthor("A");
        existing.setName("N");
        when(bookRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book patch = new Book();
        patch.setAuthor("B");
        patch.setName(null);
        Optional<Book> result = service.updateBookById(9L, patch);

        assertTrue(result.isPresent());
        assertEquals("B", existing.getAuthor());
        assertEquals("N", existing.getName(), "Name should remain unchanged");
        verify(bookRepository).save(existing);
    }

    @Test
    void updateStudentByIdReturnsEmptyWhenNotFound() {
        when(studentRepository.findById(10L)).thenReturn(Optional.empty());

        Optional<Student> result = service.updateStudentById(10L, new Student());

        assertTrue(result.isEmpty(), "Should return empty when student does not exist");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateBookByIdReturnsEmptyWhenNotFound() {
        when(bookRepository.findById(6L)).thenReturn(Optional.empty());

        Optional<Book> result = service.updateBookById(6L, new Book());

        assertTrue(result.isEmpty(), "Should return empty when book does not exist");
        verify(bookRepository, never()).save(any());
    }
}
