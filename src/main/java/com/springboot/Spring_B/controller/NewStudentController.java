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

/**
 * REST controller exposing CRUD operations for students and books with
 * comprehensive exception handling.
 */
@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class NewStudentController {

    private final StudentService studentService;

    /**
     * Creates the controller with required service dependency.
     *
     * @param studentService service handling business logic
     */
    public NewStudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    /**
     * Handles malformed request bodies.
     *
     * @param ex thrown when the request cannot be parsed
     * @return bad request response with explanation
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid input : Please check the body.");
    }


    /**
     * Converts {@link ResponseStatusException} into HTTP response.
     *
     * @param ex exception with status and message
     * @return response reflecting the provided status
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles invalid path variable types.
     *
     * @param ex argument mismatch exception
     * @return bad request response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id please give valid id");
    }

    /**
     * Generic fallback for unhandled exceptions.
     *
     * @param ex raised exception
     * @return internal server error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    /**
     * Persist a new student entity.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(addedStudent);
    }

    /**
     * Add a book to the specified student.
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
    public ResponseEntity<Student> findStudentById(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student is not Found."));
    }

    /**
     * Retrieve books owned by a specific student.
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByStudentId(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(student -> ResponseEntity.ok(student.getBooks()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not Found on this id."));
    }

    /**
     * Retrieve a specific book for a student.
     */
    @GetMapping("/{idS}/books/{idB}")
    public ResponseEntity<Book> getBookById(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        return studentService.findByStudentIdAndId(idS, idB)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not found on this id's."));

    }

    /**
     * Retrieve a book by id.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return studentService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }

    /**
     * Delete a student by id.
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
     * Delete a book of a student.
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
     * Update a book's details.
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody Book newBook) {
        return studentService.updateBookById(id, newBook)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }

}
