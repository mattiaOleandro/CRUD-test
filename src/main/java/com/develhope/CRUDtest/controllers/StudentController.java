package com.develhope.CRUDtest.controllers;

import com.develhope.CRUDtest.entities.Student;
import com.develhope.CRUDtest.repositories.I_StudentRepository;
import com.develhope.CRUDtest.services.StudentService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private I_StudentRepository i_studentRepository;

    @PostMapping
    public ResponseEntity createStudent(@RequestBody Student student){
        try {
            return ResponseEntity.ok(i_studentRepository.save(student));
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("There was a problem to create the student! \n" +
                    e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).iterator());
        }
    }

    @GetMapping
    public ResponseEntity allStudents(){
        try {
            return ResponseEntity.ok(i_studentRepository.findAll());
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("There was a problem getting all the students out! " +
                    e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).iterator());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity singleStudent(@PathVariable Long id){
        if(i_studentRepository.existsById(id)){
            try {
                return ResponseEntity.ok(i_studentRepository.findById(id));
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("There was a problem with the student extraction! \n" +
                        e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).iterator());
            }
        }else{
            return ResponseEntity.badRequest().body("The id of the student is not exists!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateIdStudent(@PathVariable Long id, @NotNull @RequestBody Student student){
        if(i_studentRepository.existsById(id)){
            try {
                //studentService.updatePrimaryKay(id, student.getId());
                student.setId(id);
                return ResponseEntity.ok(i_studentRepository.save(student));
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("There was a problem to the update of the primary key of the student! \n" +
                        e.getMessage() + "\n" + studentService.stackTraceToString(e).toString());
            }
        }else{
            return ResponseEntity.badRequest().body("The id of the student is not exists!");
        }
    }

    @PutMapping("/{id}/working")
    public ResponseEntity updateIsWorkingStudent(@PathVariable Long id, @RequestParam("working") boolean working){
        if(i_studentRepository.existsById(id)){
            try {
                return ResponseEntity.ok(studentService.updateIsWorking(id, working));
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("There was a problem to the update of the value isWorking of the student! \n" +
                        e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).iterator());
            }
        }else{
            return ResponseEntity.badRequest().body("The id of the student is not exists!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSingleStudent(@PathVariable Long id){
        if(i_studentRepository.existsById(id)){
            try {
                i_studentRepository.deleteById(id);
                return ResponseEntity.ok("The student is been deleted!");
            }catch (Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("There was a problem to delete the student! \n" +
                        e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).iterator());
            }
        }else{
            return ResponseEntity.badRequest().body("The id of the student is not exists!");
        }
    }
}
