package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private Map<Long, Student> studentMap = new HashMap<>();
    private Long id = 0L;

    public Student createStudent(Student student) {
        student.setId(id++);
        studentMap.put(student.getId(), student);
        return student;
    }

    public Student getStudentById(Long studentId) {
        if (!studentMap.containsKey(studentId)) {
            throw new StudentNotFoundException();
        }
        return studentMap.get(studentId);
    }

    public Student updateStudent(Long studentId, Student student) {
        if (!studentMap.containsKey(studentId)) {
            throw new StudentNotFoundException();
        }
        student.setId(studentId);
        studentMap.replace(studentId, student);
        return student;
    }

    public Student deleteStudent(Long studentId) {
        if (!studentMap.containsKey(studentId)) {
            throw new StudentNotFoundException();
        }
        return studentMap.remove(studentId);
    }

    public List<Student> filterByAge(int age) {
        return studentMap.values().stream()
                .filter(student -> student.getAge() == age)
                .toList();
    }
}
