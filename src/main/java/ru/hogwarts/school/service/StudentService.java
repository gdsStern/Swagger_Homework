package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        Faculty faculty = null;
        if (student.getFaculty()!=null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException());
        }
        student.setFaculty(faculty);
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public void updateStudent(Long studentId, Student student) {
        Student oldStudent = studentRepository.findById(studentId).
                orElseThrow(StudentNotFoundException::new);
        Faculty faculty = null;
        if (student.getFaculty()!=null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException());
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
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

    public List<Student> filterByRangeAge(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Faculty findStudentsFaculty(Long id) {
        return getStudentById(id).getFaculty();
    }

    public long getCountStudents() {
        return studentRepository.getCountStudents();
    }

    public double getAvgAgeStudents() {
        return studentRepository.getAvgAgeStudents();
    }

    public List<Student> getDescFiveStudents() {
        return studentRepository.getDescFiveStudents();
    }
}
