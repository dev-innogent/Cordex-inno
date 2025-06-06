package com.springboot.Spring_B.repository;

import com.springboot.Spring_B.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Student} entities providing basic CRUD operations.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
