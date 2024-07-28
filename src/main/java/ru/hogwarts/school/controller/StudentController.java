package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/student")
@RestController
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
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

    @GetMapping(params = "age")
    public List<Student> filterByAge(@RequestParam int age) {
        return studentService.filterByAge(age);
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public List<Student> filterByRangeAge(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.filterByRangeAge(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findStudentsFaculty(@PathVariable Long id) {
        return studentService.findStudentsFaculty(id);
    }
    @GetMapping("/{id}/avatarFromDb")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long id) {
        return buildResponseEntity(avatarService.getAvatarFromDb(id));
    }
    @GetMapping("/{id}/avatarFromFs")
    public ResponseEntity<byte[]> getAvatarFromFs(@PathVariable Long id) throws IOException {
        return buildResponseEntity(avatarService.getAvatarFromFs(id));
    }

    public ResponseEntity<byte[]> buildResponseEntity(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }
}
