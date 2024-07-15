package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        if (Objects.isNull(student)) {
            throw new StudentNotFoundException();
        }
        return studentRepository.save(student);
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId).get();
    }

    public void updateStudent(Long studentId, Student student) {
        Student oldStudent = studentRepository.findById(studentId).
                orElseThrow(StudentNotFoundException::new);
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        studentRepository.save(oldStudent);
    }

    public Student deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).
                orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(student);
        return student;
    }

    public List<Student> filterByAge(int age) {
        return studentRepository.findAllByAge(age);
    }
}
