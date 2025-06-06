package com.springboot.Spring_B.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.List;
@Entity
@Table(name="student")
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    @OneToMany(mappedBy = "student",fetch = FetchType.LAZY)// to stop loop of excecuting queries
    /* This annotation tells Jackson to omit the serialization of the author field when serializing Book objects to JSON.
     The purpose of this is to avoid circular references and infinite loops during serialization.
      Without @JsonBackReference, when serializing an Author object, Jackson would include the associated Book objects,
      and when serializing a Book object, it would include the associated Author object, leading to a circular reference.*/
    @JsonManagedReference
    private List<Book> books;

    public Student() {
    }

    public Student(String name, String email, LocalDate dob, List<Book> books) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.books = books;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }


    /*To  remove the default representation like "com.springboot.Spring_B.entity.Book@60e1d0a0 we ovveride it as
     and to handle StackOverFlow Exception*/
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.SHORT_PREFIX_STYLE)// to remove unusble code

                .append("Student{id = '", id)
                .append("', name = '", name) // Prevent circular reference
                .append("', email = '", email)
                .append("',dob = '", dob)
                .append("',book = '", books)
                .append("'}")
                .toString();
        /*return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", books=" + books +
                '}';
            */
    }

}
