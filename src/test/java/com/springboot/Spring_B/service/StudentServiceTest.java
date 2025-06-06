package com.springboot.Spring_B.service;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.repository.BookRepository;
import com.springboot.Spring_B.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private StudentService studentService;

    @Captor
    private ArgumentCaptor<Student> studentCaptor;
    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    private Student student;
    private Book book;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setName("John");
        student.setEmail("john@example.com");
        student.setDob(LocalDate.now());

        book = new Book();
        book.setName("Book1");
        book.setAuthor("Author1");
    }

    @Test
    void addStudent_initializesBookListAndSaves() {
        student.setBooks(null);

        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.addStudent(student);

        assertThat(saved.getBooks()).isNotNull();
        verify(studentRepository).save(studentCaptor.capture());
        assertThat(studentCaptor.getValue().getBooks()).isNotNull();
    }

    @Test
    void addStudent_assignsStudentToEachBook() {
        Book b = new Book();
        student.setBooks(new ArrayList<>(Collections.singletonList(b)));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.addStudent(student);

        assertThat(saved.getBooks().get(0).getStudent()).isEqualTo(saved);
    }

    @Test
    void addBook_returnsSavedBookWhenStudentExists() {
        student.setBooks(new ArrayList<>());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Book> result = studentService.addBook(1L, book);

        assertThat(result).containsSame(book);
        assertThat(student.getBooks()).contains(book);
        verify(bookRepository).save(bookCaptor.capture());
        assertThat(bookCaptor.getValue().getStudent()).isEqualTo(student);
    }

    @Test
    void addBook_returnsEmptyWhenStudentMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Book> result = studentService.addBook(1L, book);

        assertThat(result).isEmpty();
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteStudentById_removesBooksAndStudentWhenPresent() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        boolean deleted = studentService.deleteStudentById(1L);

        assertThat(deleted).isTrue();
        verify(bookRepository).deleteByStudentId(1L);
        verify(studentRepository).deleteById(1L);
    }

    @Test
    void deleteStudentById_returnsFalseWhenMissing() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        boolean deleted = studentService.deleteStudentById(1L);

        assertThat(deleted).isFalse();
        verify(bookRepository, never()).deleteByStudentId(anyLong());
        verify(studentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteBookByStudentIdAndId_deletesWhenBothExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));

        boolean deleted = studentService.deleteBookByStudentIdAndId(1L, 2L);

        assertThat(deleted).isTrue();
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBookByStudentIdAndId_returnsFalseWhenStudentMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        boolean deleted = studentService.deleteBookByStudentIdAndId(1L, 2L);

        assertThat(deleted).isFalse();
        verify(bookRepository, never()).delete(any());
    }

    @Test
    void updateStudentById_updatesFieldsWhenPresent() {
        Student updated = new Student();
        updated.setName("Jane");
        updated.setEmail("jane@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Student> result = studentService.updateStudentById(1L, updated);

        assertThat(result).isPresent();
        assertThat(student.getName()).isEqualTo("Jane");
        assertThat(student.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void updateStudentById_returnsEmptyWhenMissing() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.updateStudentById(1L, student);

        assertThat(result).isEmpty();
        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateBookById_updatesFieldsWhenPresent() {
        Book newBook = new Book();
        newBook.setAuthor("NewAuthor");

        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Book> result = studentService.updateBookById(2L, newBook);

        assertThat(result).isPresent();
        assertThat(book.getAuthor()).isEqualTo("NewAuthor");
    }

    @Test
    void updateBookById_returnsEmptyWhenMissing() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Book> result = studentService.updateBookById(2L, book);

        assertThat(result).isEmpty();
        verify(bookRepository, never()).save(any());
    }
}
