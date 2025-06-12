package com.springboot.Spring_B.controller;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Alternative controller demonstrating explicit error handling for student and
 * book operations. Exposes similar endpoints as {@link StudentController} but
 * throws {@link ResponseStatusException} on failures instead of returning
 * {@code 404} responses directly.
 */
@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class NewStudentController {

    private final StudentService studentService;

    /**
     * Construct controller with required service.
     *
     * @param studentService business service
     */
    @Autowired
    public NewStudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    /**
     * Handle malformed JSON requests.
     *
     * @param ex thrown exception
     * @return bad request response with human readable message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid input : Please check the body.");
    }


    /**
     * Return the status and message from a thrown {@link ResponseStatusException}.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handle path variable conversion errors.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id please give valid id");
    }

    /**
     * Fallback handler for any uncaught exception.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    /**
     * Create a new student.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(addedStudent);
    }

    /**
     * Add a book to the given student.
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
     * Retrieve all books owned by a specific student.
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByStudentId(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(student -> ResponseEntity.ok(student.getBooks()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not Found on this id."));
    }

    /**
     * Retrieve a specific book of a student.
     */
    @GetMapping("/{idS}/books/{idB}")
    public ResponseEntity<Book> getBookById(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        return studentService.findByStudentIdAndId(idS, idB)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not found on this id's."));
    }

    /**
     * Find a book without referencing its student.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return studentService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }

    /**
     * Delete a student along with all of its books.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Long id) {
        boolean isDelete = studentService.deleteStudentById(id);
        if (isDelete) {
            return ResponseEntity.ok("Student is deleted");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Exists.");
    }

    /**
     * Delete a specific book of a student.
     */
    @DeleteMapping("/{idS}/books/{idB}")
    public ResponseEntity<String> deleteBookByStudentIdAndId(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        boolean isDeleted = studentService.deleteBookByStudentIdAndId(idS, idB);
        if (isDeleted) {
            return ResponseEntity.ok("Book is deleted");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book isn't deleted, Check inputs.");
    }

    /**
     * Update mutable fields of a student.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable("id") Long id, @RequestBody Student newStudent) {
        return studentService.updateStudentById(id, newStudent)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student is not Found."));
    }

    /**
     * Update a book entity by its id.
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody Book newBook) {
        return studentService.updateBookById(id, newBook)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }

}
