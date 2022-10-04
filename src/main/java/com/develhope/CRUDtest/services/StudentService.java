package com.develhope.CRUDtest.services;

import com.develhope.CRUDtest.entities.Student;
import com.develhope.CRUDtest.repositories.I_StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private I_StudentRepository i_studentRepository;

    public Student updateIsWorking(Long id, boolean isWorking) throws Exception {
        if (!i_studentRepository.existsById(id)) throw new Exception("The student not exist!");
        Optional<Student> student = i_studentRepository.findById(id);
        student.get().setWorking(isWorking);
        return i_studentRepository.save(student.get());
    }

    public Student updatePrimaryKay(Long id, Long studentId) throws Exception {
        if (!i_studentRepository.existsById(id)) throw new Exception("The student not exist!");
        Student student = i_studentRepository.findById(id).get();
        student.setId(studentId);
        //student.setName(i_studentRepository.findById(id).get().getName());
        //student.setSurname(i_studentRepository.findById(id).get().getSurname());
        //student.setWorking(i_studentRepository.findById(id).get().isWorking());
        i_studentRepository.save(student);
        return student;
    }
    
    public StackTraceElement stackTraceToString(Exception e){
        StackTraceElement stringStackTrace = null;
        for (int i = 0; i < e.getStackTrace().length; i++) {
            stringStackTrace = Arrays.stream(e.getStackTrace()).toList().get(i);
        }
        return stringStackTrace;
    }
}
