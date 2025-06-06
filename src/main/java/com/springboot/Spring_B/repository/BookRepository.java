package com.springboot.Spring_B.repository;

import com.springboot.Spring_B.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*Purpose: The primary purpose of the @Repository annotation is to mark a class as a data access object (DAO).
A DAO is responsible for encapsulating the interaction with a data store, such as a database and abstracting
the details of data access.*/
@Repository
/*
JpaRepository is a generic interface that provides a set of methods for common CRUD (Create, Read, Update,
 Delete)operations on entities. In this case, it's being used for the Book entity
 and that the primary key of the Book entity is of type Long.and it has additional functionality rather than
 CrudRepository  it provides customization options, It allows us to generate queries based on method names.
 For example, you can define a method like findByFirstNameAndLastName(String firstName, String lastName)
 and another example is deleteByStudentId(long studentId) we used it*/
public interface BookRepository extends JpaRepository<Book,Long>
{
    Optional<Book> findByStudentIdAndId(Long studentId, Long bookId);// to find book corresponding to both id's.
    @Transactional//used to perform some database operations here
    void deleteByStudentId(long studentId);//here we deleted the rows corresponding to studentId

}

//multiple attribute se data find karvana h jse name an year ke corresponding
//responseENTIYY and implement service transaction management //with acid property