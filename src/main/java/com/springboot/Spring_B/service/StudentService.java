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
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Student addStudent(Student student)
    {
        if (student.getBooks() == null) {
            student.setBooks(new ArrayList<>()); // Initialize with an empty list if it's null
        }

        List<Book> bookList = student.getBooks();
        bookList.forEach(b -> b.setStudent(student)); // For each student, set the current teacher

        student.setBooks(bookList); // Set the list of students for this teacher
        return studentRepository.save(student);
    }

@Transactional
    public Book addBook(long id, Book book) {
        Student student = findStudentById(id);
        if (student!= null) {
            book.setStudent(student);//foreign key added
            student.getBooks().add(book);
            return bookRepository.save(book);
        }
        return null;
    }

    public Student findStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }


    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public boolean deleteStudentById(Long id) {
        try {
            bookRepository.deleteByStudentId(id);
            studentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean deleteBookByStudentIdAndId(Long idS, Long idB) {
        Student student = findStudentById(idS);
        if(student != null)
        {
            Book book = findBookById(idB);
            if(book != null)
            {
                bookRepository.delete(book);
                return  true;
            }
        }
        return false;
    }

    public Book findByStudentIdAndId(Long idS, Long idB)// here first parameter is book id mind it
    {
        return bookRepository.findByStudentIdAndId(idS, idB).orElse(null);
    }

    @Transactional
    public Student updateStudentById(Long id, Student newStudent)// here first parameter is book id mind it
    {
        Student student = findStudentById(id);
        if (student != null) {
            if (newStudent.getName() != null)
                student.setName(newStudent.getName());
            if (newStudent.getEmail() != null)
                student.setEmail(newStudent.getEmail());
            if (newStudent.getDob() != null)
                student.setDob(newStudent.getDob());
            if (newStudent.getBooks() != null)
                student.setBooks(newStudent.getBooks());
            studentRepository.save(student);
            return student;
        }
        return null;
    }
@Transactional
    public Book updateBookById(Long id, Book newBook)// here first parameter is book id mind it
    {
        Book book = findBookById(id);
        if (book != null) {
            if (newBook.getAuthor() != null)
                book.setAuthor(newBook.getAuthor());
            if (newBook.getName() != null)
                book.setName(newBook.getName());
            return bookRepository.save(book);
        } else
            return null;
    }
}

