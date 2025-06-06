package com.springboot.Spring_B.service;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.repository.BookRepository;
import com.springboot.Spring_B.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, BookRepository bookRepository) {
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Student addStudent(Student student) {
        if (student.getBooks() == null) {
            student.setBooks(new ArrayList<>());
        }
        student.getBooks().forEach(b -> b.setStudent(student));
        return studentRepository.save(student);
    }

    @Transactional
    public Optional<Book> addBook(long id, Book book) {
        return findStudentById(id).map(student -> {
            book.setStudent(student);
            student.getBooks().add(book);
            return bookRepository.save(book);
        });
    }

    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }


    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public boolean deleteStudentById(Long id) {
        if (studentRepository.existsById(id)) {
            bookRepository.deleteByStudentId(id);
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteBookByStudentIdAndId(Long idS, Long idB) {
        return findStudentById(idS)
                .flatMap(s -> findBookById(idB))
                .map(b -> {
                    bookRepository.delete(b);
                    return true;
                })
                .orElse(false);
    }

    public Optional<Book> findByStudentIdAndId(Long idS, Long idB) {
        return bookRepository.findByStudentIdAndId(idS, idB);
    }

    @Transactional
    public Optional<Student> updateStudentById(Long id, Student newStudent) {
        return findStudentById(id).map(student -> {
            if (newStudent.getName() != null)
                student.setName(newStudent.getName());
            if (newStudent.getEmail() != null)
                student.setEmail(newStudent.getEmail());
            if (newStudent.getDob() != null)
                student.setDob(newStudent.getDob());
            if (newStudent.getBooks() != null)
                student.setBooks(newStudent.getBooks());
            return studentRepository.save(student);
        });
    }
    @Transactional
    public Optional<Book> updateBookById(Long id, Book newBook) {
        return findBookById(id).map(book -> {
            if (newBook.getAuthor() != null)
                book.setAuthor(newBook.getAuthor());
            if (newBook.getName() != null)
                book.setName(newBook.getName());
            return bookRepository.save(book);
        });
    }
}

