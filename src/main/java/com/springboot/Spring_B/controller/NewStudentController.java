package com.springboot.Spring_B.controller;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
/**
 * REST controller exposing CRUD operations with additional exception handling.
 */
public class NewStudentController {
    private final StudentService studentService;

    /**
     * Creates a new controller with the required service dependency.
     *
     * @param studentService service used to perform business operations
     */
    public NewStudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // Handle the custom exception and return an appropriate response
        return ResponseEntity.badRequest().body("Invalid input : Please check the body.");
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id please give valid id");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    /**
     * Create a new student entity.
     *
     * @param student payload representing the student
     * @return the created student
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(addedStudent);
    }

    /**
     * Add a book to a student's collection.
     *
     * @param id   student identifier
     * @param book book to add
     * @return created book or NOT_FOUND when the student does not exist
     */
    @PostMapping("/{id}/book")
    public ResponseEntity<Book> addBook(@PathVariable long id, @RequestBody Book book) {
        return studentService.addBook(id, book)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not added bcz of invalid Id."));
    }

    /**
     * Retrieve all students.
     */
    @GetMapping
    public ResponseEntity<List<Student>> findAllStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);

    }

    /**
     * Retrieve all books in the system.
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books;
        books = studentService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Retrieve a student by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student is not Found."));
    }

    /**
     * Retrieve all books for a given student.
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByStudentId(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(student -> ResponseEntity.ok(student.getBooks()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not Found on this id."));
    }

    /**
     * Retrieve a particular book of a student by book id.
     */
    @GetMapping("/{idS}/books/{idB}")
    public ResponseEntity<Book> getBookById(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        return studentService.findByStudentIdAndId(idS, idB)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not found on this id's."));

    }

    /**
     * Retrieve a book by its id.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return studentService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }

    /**
     * Delete a particular student by its id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Long id) {
        boolean isDelete = studentService.deleteStudentById(id);
        if (isDelete)
            return ResponseEntity.ok("Student is deleted");
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Exists.");
    }

    /**
     * Delete a particular book of a student by its id.
     */
    @DeleteMapping("/{idS}/books/{idB}")
    public ResponseEntity<String> deleteBookByStudentIdAndId(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        boolean isDeleted = studentService.deleteBookByStudentIdAndId(idS, idB);
        if (isDeleted)
            return ResponseEntity.ok("Book is deleted");
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book isn't deleted, Check inputs.");
    }

    /**
     * Update student details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable("id") Long id, @RequestBody Student newStudent) {
        return studentService.updateStudentById(id, newStudent)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student is not Found."));
    }

    /**
     * Update a student's book.
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody Book newBook) {
        return studentService.updateBookById(id, newBook)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }

}
