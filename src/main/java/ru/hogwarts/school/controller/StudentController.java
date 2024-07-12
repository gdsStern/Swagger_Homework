package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Student student) {
        studentService.updateStudent(id, student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @DeleteMapping("/{id}")
    public Student remove(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping
    public List<Student> filterByAge(@RequestParam int age) {
        return studentService.filterByAge(age);
    }
}
