package com.springboot.Spring_B.controller;

/**
 * REST controller with explicit exception handling exposing CRUD operations for
 * students and their books. This class mirrors {@link StudentController} but
 * demonstrates how to map specific exceptions to HTTP responses.
 */

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

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class NewStudentController {

    private final StudentService studentService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param studentService service used to perform operations
     */
    @Autowired
    public NewStudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    /**
     * Handles malformed JSON payloads.
     *
     * @param ex thrown exception
     * @return response indicating invalid input
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid input : Please check the body.");
    }


    /**
     * Converts {@link ResponseStatusException} to an HTTP response.
     *
     * @param ex the thrown exception
     * @return a response with the original status and message
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles invalid parameter formats in the request path.
     *
     * @param ex conversion failure
     * @return bad request response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id please give valid id");
    }

    /**
     * Fallback handler for unexpected errors.
     *
     * @param ex uncaught exception
     * @return internal server error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

    /**
     * Persist a new student.
     *
     * @param student payload representing the new student
     * @return created student
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(addedStudent);
    }

    /**
     * Add a new book to the specified student.
     *
     * @param id   student identifier
     * @param book book to persist
     * @return persisted book or 404 if the student is missing
     */
    @PostMapping("/{id}/book")
    public ResponseEntity<Book> addBook(@PathVariable long id, @RequestBody Book book) {
        return studentService.addBook(id, book)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not added bcz of invalid Id."));
    }

    /**
     * Retrieve every student in the system.
     *
     * @return list of students
     */
    @GetMapping
    public ResponseEntity<List<Student>> findAllStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);

    }

    /**
     * Retrieve all books across all students.
     *
     * @return list of books
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = studentService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Find a student by identifier.
     *
     * @param id student id
     * @return student or 404 if missing
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> findStudentById(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student is not Found."));
    }

    /**
     * Retrieve all books belonging to a particular student.
     *
     * @param id student identifier
     * @return list of the student's books or 404 if not found
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
     *
     * @param idS student id
     * @param idB book id
     * @return the book or 404 if either entity is missing
     */
    @GetMapping("/{idS}/books/{idB}")
    public ResponseEntity<Book> getBookById(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        return studentService.findByStudentIdAndId(idS, idB)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not found on this id's."));
    }

    /**
     * Retrieve a book by its identifier.
     *
     * @param id book id
     * @return the book or 404 if absent
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
     *
     * @param id student identifier
     * @return confirmation or 404 status
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
     * Delete a particular book of a student by book id.
     *
     * @param idS student id
     * @param idB book id
     * @return confirmation or 404 status
     */
    @DeleteMapping("/{idS}/books/{idB}")
    public ResponseEntity<String> deleteBookByStudentIdAndId(@PathVariable("idS") Long idS,
                                                             @PathVariable("idB") Long idB) {
        boolean isDeleted = studentService.deleteBookByStudentIdAndId(idS, idB);
        if (isDeleted)
            return ResponseEntity.ok("Book is deleted");
        else  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book isn't deleted, Check inputs.");
    }
    /**
     * Update mutable fields of a student.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable("id") Long id,
                                                     @RequestBody Student newStudent) {
        return studentService.updateStudentById(id, newStudent)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student is not Found."));
    }

    /**
     * Update a book's details by id.
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody Book newBook) {
        return studentService.updateBookById(id, newBook)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Book is not Found."));
    }
}
