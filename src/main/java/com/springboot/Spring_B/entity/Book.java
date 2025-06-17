package com.springboot.Spring_B.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Entity representing a book owned by a {@link Student}.
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String author;
    @ManyToOne
    /*
     * Mark this side as the non-owning side and omit it from serialization to
     * prevent cycles.
     */
    @JsonBackReference
    private Student student;

    /**
     * @return book title
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new book title
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return owner student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student owner student
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * @return identifier
     */
    public Long getId() {
        return id;
    }

    /*
     * Custom toString to avoid verbose output and potential circular references
     * when logging entities.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("Book{id = '", id)
                .append("', name = '", name)
                .append("', author = '", author)
                .append("'}")
                .toString();
    }
}
