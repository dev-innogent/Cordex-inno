package com.springboot.Spring_B.controller;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/*It combines the @Controller and @ResponseBody annotations, providing a convenient way to create
 RESTful APIs that return data in various formats (e.g., JSON, XML) directly to the client*/
@RestController
@CrossOrigin("*")
@RequestMapping("/student")
public class StudentController
{
    @GetMapping("/hello")
    public String get()
    {
        return "hello";
    }
    @GetMapping("/helloResponse")
    public ResponseEntity getResponseEntity()
    {
        return ResponseEntity.ok().body("Hello Response is ok");
    }
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Add a new Student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student saved = studentService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Add a book to the Student
    @PostMapping("/{id}/book")
    public ResponseEntity<Book> addBook(@PathVariable long id, @RequestBody Book book) {
        return studentService.addBook(id, book)
                .map(b -> ResponseEntity.status(HttpStatus.CREATED).body(b))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);
    }
    // Get all books
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = studentService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    // Get student by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Get all books of a student
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByStudentId(@PathVariable("id") Long id) {
        return studentService.findStudentById(id)
                .map(student -> ResponseEntity.ok(student.getBooks()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get a particular book of a student
    @GetMapping("/{idS}/books/{idB}")
    public ResponseEntity<Book> getBookById(@PathVariable("idS") Long idS, @PathVariable("idB") Long idB) {
        return studentService.findByStudentIdAndId(idS, idB)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Get book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return studentService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a Student by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        boolean deleted = studentService.deleteStudentById(id);
        if (deleted) {
            return ResponseEntity.ok("Student is deleted");
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a particular book of a Student by id
    @DeleteMapping("/{idS}/books/{idB}")
    public ResponseEntity<String> deleteByStudentIdAndId(@PathVariable("idS") Long idS,
                                                         @PathVariable("idB") Long idB) {
        boolean deleted = studentService.deleteBookByStudentIdAndId(idS, idB);
        if (deleted) {
            return ResponseEntity.ok("book is deleted");
        }
        return ResponseEntity.notFound().build();
    }

    // Update student details
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable("id") Long id,
                                                     @RequestBody Student newStudent) {
        return studentService.updateStudentById(id, newStudent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Update book details
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody Book newBook) {
        return studentService.updateBookById(id, newBook)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}

