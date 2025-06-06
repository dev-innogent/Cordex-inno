package com.springboot.Spring_B.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity representing a student which can own multiple books.
 */
@Entity
@Table(name = "student")
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    /*
     * Manage serialization of the book list to avoid circular references when
     * converting entities to JSON.
     */
    @JsonManagedReference
    private List<Book> books;

    /**
     * Default constructor required by JPA.
     */
    public Student() {
    }

    /**
     * Constructs a new student with provided details.
     *
     * @param name  student's name
     * @param email student's email
     * @param dob   date of birth
     * @param books initial list of books
     */
    public Student(String name, String email, LocalDate dob, List<Book> books) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.books = books;
    }

    /**
     * @return generated identifier
     */
    public Long getId() {
        return id;
    }


    /**
     * @return student's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new student name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return student's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @param dob new date of birth
     */
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    /**
     * @return books linked to the student
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books list of books to assign
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }


    /*
     * Custom toString to avoid verbose output and potential circular references
     * when logging entities.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("Student{id = '", id)
                .append("', name = '", name)
                .append("', email = '", email)
                .append("',dob = '", dob)
                .append("',book = '", books)
                .append("'}")
                .toString();
    }

}
