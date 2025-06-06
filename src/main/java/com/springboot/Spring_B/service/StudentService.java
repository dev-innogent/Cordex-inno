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

/**
 * Service layer containing business logic for managing students and books.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, BookRepository bookRepository) {
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Persist a new student along with its books.
     *
     * @param student entity to persist
     * @return saved student
     */
    @Transactional
    public Student addStudent(Student student) {
        if (student.getBooks() == null) {
            student.setBooks(new ArrayList<>());
        }
        student.getBooks().forEach(b -> b.setStudent(student));
        return studentRepository.save(student);
    }

    /**
     * Add a new book to a student's collection.
     *
     * @param id   id of the student
     * @param book book to add
     * @return optional containing saved book if student exists
     */
    @Transactional
    public Optional<Book> addBook(long id, Book book) {
        return findStudentById(id).map(student -> {
            book.setStudent(student);
            student.getBooks().add(book);
            return bookRepository.save(book);
        });
    }

    /**
     * Find a student by id.
     *
     * @param id student identifier
     * @return optional student
     */
    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Find a book by id.
     *
     * @param id book identifier
     * @return optional book
     */
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }


    /**
     * Retrieve all students.
     */
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieve all books.
     */
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Delete a student and all its books if it exists.
     *
     * @param id student id
     * @return {@code true} if a student was deleted
     */
    @Transactional
    public boolean deleteStudentById(Long id) {
        if (studentRepository.existsById(id)) {
            bookRepository.deleteByStudentId(id);
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Delete a specific book of a student.
     *
     * @param idS student id
     * @param idB book id
     * @return {@code true} if the book was deleted
     */
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

    /**
     * Retrieve a book for a given student.
     */
    public Optional<Book> findByStudentIdAndId(Long idS, Long idB) {
        return bookRepository.findByStudentIdAndId(idS, idB);
    }

    /**
     * Update mutable fields of a student.
     */
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

    /**
     * Update a book entity by id.
     */
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

