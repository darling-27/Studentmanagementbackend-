package com.demo.demo.repository;

import com.demo.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByRollNo(String rollNo);
    Optional<Student> findByUser_Username(String username);
    Optional<Student> findByUser_Id(Long userId);
}
