package com.develhope.CRUDtest;

import com.develhope.CRUDtest.entities.Student;
import com.develhope.CRUDtest.repositories.I_StudentRepository;
import com.develhope.CRUDtest.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private I_StudentRepository i_studentRepository;

    @Test
    void checkStudentActivation() throws Exception {
        Student student = new Student();
        student.setName("Mattia");
        student.setSurname("Oleandro");
        student.setWorking(true);

        Student studentFromDB = i_studentRepository.save(student);
        assertThat(studentFromDB).isNotNull();
        assertThat(studentFromDB.getId()).isNotNull();

        Student studentFromService = studentService.updateIsWorking(student.getId(), true);
        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService.getId()).isNotNull();
        assertThat(studentFromService.isWorking()).isTrue();

        Student studentFromFind = i_studentRepository.findById(studentFromDB.getId()).get();
        assertThat(studentFromFind).isNotNull();
        assertThat(studentFromFind.getId()).isNotNull();
        assertThat(studentFromFind.getId()).isEqualTo(studentFromDB.getId());
        assertThat(studentFromFind.isWorking()).isTrue();
    }
}
