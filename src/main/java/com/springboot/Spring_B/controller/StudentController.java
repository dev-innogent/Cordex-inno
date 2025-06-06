package com.springboot.Spring_B.controller;

import com.springboot.Spring_B.entity.Book;
import com.springboot.Spring_B.entity.Student;
import com.springboot.Spring_B.repository.BookRepository;
import com.springboot.Spring_B.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private StudentRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    //for adding Student
    @PostMapping
    public  Student addStudent( @RequestBody Student student)
    {
        userRepository.save(student);
        return student;
    }

    //for adding a Book in a Student List of books
    @PostMapping("/{id}/book")
    public  Book addBook(@PathVariable long id, @RequestBody Book book)
    {
        Optional<Student> user = userRepository.findById(id);
        if(user.isPresent()) {
            Student student = user.get();
            book.setStudent(student);//foreign key added
            student.getBooks().add(book);
            bookRepository.save(book);
            return book;
        }
        return null;
        /*
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
        */
    }

    //for finding all Students
    //@GetMapping("/allStudents")
    @GetMapping
    public List<Student> getStudents()
    {
        return userRepository.findAll();

    }
    //for finding all Books
    @GetMapping("/books")
    public List<Book> getBooks()
    {
        return bookRepository.findAll();

    }

    //for finding the Student by id
    //@GetMapping("/student/{id}")
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable("id") Long id)//here pathVariable comes from url and we assign in id
    {
        try {
            Optional<Student> user = userRepository.findById(id);
            return user.get();
        }
        catch(Exception e)
        {
            return null;
        }

    }


    //for finding all Books of a particular Student
    @GetMapping("/{id}/books")
    public List<Book> getBooksByStudentId(@PathVariable("id") Long id)//here pathVariable comes from url and we assign in id
    {
        try {
            Optional<Student> user = userRepository.findById(id);
            if(user.isPresent())
            {
                Student newStudent = user.get();
                return newStudent.getBooks();
            }
            else return null;
        }
        catch(Exception e) {
            return null;
        }
    }

    //for finding a particular book of a Student by book id
    @GetMapping("/{idS}/books/{idB}")
    public Book getBookById(@PathVariable("idS") Long idS,@PathVariable("idB") Long idB)//here pathVariable comes from url and we assign in id
    {
        try {
            Optional<Student> user = userRepository.findById(idS);
            //System.out.println(idS+"-"+idB);
            if(user.isPresent())
            {
                //System.out.println("yes");
                Student newStudent =  user.get();
                Optional<Book> book = bookRepository.findById(idB);
                return book.get();
            }
            else
            {
                //System.out.println("no");
                return  null;
            }
        }
        catch(Exception e)
        {
           // return null;
        }
        return null;

    }


    //for finding the book by id
    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable("id") Long id)//here pathVariable comes from url and we assign in id
    {
        try {
            Optional<Book> book = bookRepository.findById(id);
            if(book.isPresent()) {
                return book.get();
            }
            else return  null;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    // delete a particular Student by its id
    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Long id)
    {
        try
        {
            bookRepository.deleteByStudentId(id);
            userRepository.deleteById(id);
            return "Student is deleted";
        }
        catch(Exception e)
        {
        }
        return "User not Found";
    }

    // delete a particular book of a Student by its book id
    @DeleteMapping("/{idS}/books/{idB}")
    public String deleteByStudentIdAndId(@PathVariable("idS") Long idS,@PathVariable("idB") Long idB)
    {
        try
        {
            Optional<Student> student = userRepository.findById(idS);
            if(student.isPresent()) {
                Optional<Book> book = bookRepository.findById(idB);
                if(book.isPresent()) {
                    bookRepository.delete(book.get());
                    return "book is deleted";
                }
            }
            return "book isn't deleted check id's";
        }
        catch(Exception e)
        {
            return "Book not Found";
        }
    }

    // update student details which we want to update by its id.
    @PutMapping("/{id}")
    public Student updateStudentById(@PathVariable("id") Long id,@RequestBody Student newStudent)
    {
        try {
            Optional<Student> student1 = userRepository.findById(id);
            if(student1.isPresent()) {
                Student student = student1.get();
                if(newStudent.getName()!=null)
                    student.setName(newStudent.getName());
                if(newStudent.getEmail()!=null)
                     student.setEmail(newStudent.getEmail());
                if(newStudent.getDob()!=null)
                      student.setDob(newStudent.getDob());
                if(newStudent.getBooks()!=null)
                     student.setBooks(newStudent.getBooks());

                return  userRepository.save(student);
            }
        }
        catch(Exception e) {
            return null;
        }
        return null;
    }


    // update student's book details which we want to update by book id.
    @PutMapping("/book/{id}")
    public Book updateBookById(@PathVariable("id") Long id,@RequestBody Book newBook)
    {
        try {
            Optional<Book> book1 = bookRepository.findById(id);
            if(book1.isPresent())
            {
                Book book = book1.get();
                if(newBook.getAuthor()!=null)
                    book.setAuthor(newBook.getAuthor());
                if(newBook.getName()!=null)
                    book.setName(newBook.getName());
                return  bookRepository.save(book);
            }
        }
        catch(Exception e) {
            return null;
        }
        return null;
    }


}

