/*
 * Spring Data repository for Book entities.
 */
package com.springboot.Spring_B.repository;

import com.springboot.Spring_B.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Book} entities providing CRUD operations and custom
 * queries used by the service layer.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Retrieve a book for a particular student.
     *
     * @param studentId id of the owning student
     * @param bookId    id of the book
     * @return optional of the found book
     */
    Optional<Book> findByStudentIdAndId(Long studentId, Long bookId);

    /**
     * Delete all books belonging to the given student.
     *
     * @param studentId id of the student whose books will be removed
     */
    @Transactional
    void deleteByStudentId(long studentId);
}
