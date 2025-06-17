package com.springboot.Spring_B.controller;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST controller exposing CRUD operations for students and books.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/student")
public class StudentController {
    /** Sample endpoint used for testing the service */
    @GetMapping("/hello")
    public String get() {
        return "hello";
    }

    /** Another simple endpoint returning a ResponseEntity */
    @GetMapping("/helloResponse")
    public ResponseEntity<String> getResponseEntity() {
        return ResponseEntity.ok().body("Hello Response is ok");
    }
    private final StudentService studentService;

    /**
     * Constructs the controller with the required service.
     *
     * @param studentService business service
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Create a new student.
     *
     * @param student payload representing the student
     * @return created student
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student saved = studentService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Add a book to the specified student.
     */
    @PostMapping("/{id}/book")
    public ResponseEntity<Book> addBook(@PathVariable long id, @RequestBody Book book) {
        return studentService.addBook(id, book)
                .map(b -> ResponseEntity.status(HttpStatus.CREATED).body(b))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieve all students.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);
    }
    /**
     * Retrieve all books.
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = studentService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Retrieve a student by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Retrieve all books for a given student.
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByStudentId(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(student -> ResponseEntity.ok(student.getBooks()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieve a particular book for a student.
     */
    @GetMapping("/{idS}/books/{idB}")
    public ResponseEntity<Book> getBookById(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        return studentService.findByStudentIdAndId(idS, idB)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Retrieve a book by id without specifying the student.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return studentService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a student by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        boolean deleted = studentService.deleteStudentById(id);
        if (deleted) {
            return ResponseEntity.ok("Student is deleted");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a particular book of a student.
     */
    @DeleteMapping("/{idS}/books/{idB}")
    public ResponseEntity<String> deleteByStudentIdAndId(@PathVariable("idS") Long idS,
                                                         @PathVariable("idB") Long idB) {
        boolean deleted = studentService.deleteBookByStudentIdAndId(idS, idB);
        if (deleted) {
            return ResponseEntity.ok("book is deleted");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update an existing student's details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable("id") Long id,
                                                     @RequestBody Student newStudent) {
        return studentService.updateStudentById(id, newStudent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Update a book's details.
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody Book newBook) {
        return studentService.updateBookById(id, newBook)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}

