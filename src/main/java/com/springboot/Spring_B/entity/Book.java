package com.springboot.Spring_B.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

@Entity//It represents a table in a relational database.
public class Book
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)@Column(name ="id",nullable = false)
    private Long id;
    private String name;
    private String author;
    @ManyToOne // to make relationship with another entity
    /*It indicates that this side of the relationship should be considered the "non-owning" side
    and should not be included when serializing to JSON.It is also used to break the loop*/
    @JsonBackReference
    private Student student;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    /*To  remove the default representation like "com.springboot.Spring_B.entity.Book@60e1d0a0 we ovveride it as
     and to handle StackOverFlow Exception*/
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.SHORT_PREFIX_STYLE)// to remove unusble code
                .append("Book{id = '", id)
                .append("', name = '", name) // Prevent circular reference
                .append("', author = '", author)
                .append("'}")
                .toString();
    }
/*
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", student=" + student +
                '}';
    }
 */
}
