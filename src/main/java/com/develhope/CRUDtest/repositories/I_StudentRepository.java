package com.develhope.CRUDtest.repositories;

import com.develhope.CRUDtest.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I_StudentRepository extends JpaRepository<Student, Long> {
}
