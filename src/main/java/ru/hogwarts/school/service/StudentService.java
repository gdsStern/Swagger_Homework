package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Вызван метод createStudent");
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("Не найден факультет с id " + student.getFaculty().getId());
                        return new FacultyNotFoundException();
                    });
        }
        student.setFaculty(faculty);
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student getStudentById(Long studentId) {
        logger.info("Вызван метод getStudentById");
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public void updateStudent(Long studentId, Student student) {
        logger.info("Вызван метод updateStudent");
        Student oldStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> {
            logger.error("Не найден студент с id " + studentId);
            return new StudentNotFoundException();
        });
        Faculty faculty = null;
        if (student.getFaculty()!=null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("Не найден факультет с id " + student.getFaculty().getId());
                        return new FacultyNotFoundException();
                    });
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        studentRepository.save(oldStudent);
    }

    public Student deleteStudent(Long studentId) {
        logger.info("Вызван метод deleteStudent");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
            logger.error("Не найден студент с id " + studentId);
            return new StudentNotFoundException();
        });
        studentRepository.delete(student);
        return student;
    }

    public List<Student> filterByAge(int age) {
        logger.info("Вызван метод filterByAge");
        return studentRepository.findAllByAge(age);
    }

    public List<Student> filterByRangeAge(int minAge, int maxAge) {
        logger.info("Вызван метод filterByRangeAge");
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Faculty findStudentsFaculty(Long id) {
        logger.info("Вызван метод findStudentsFaculty");
        return getStudentById(id).getFaculty();
    }

    public long getCountStudents() {
        logger.info("Вызван метод getCountStudents");
        return studentRepository.getCountStudents();
    }

    public double getAvgAgeStudents() {
        logger.info("Вызван метод getAvgAgeStudents");
        return studentRepository.getAvgAgeStudents();
    }

    public List<Student> getDescFiveStudents() {
        logger.info("Вызван метод getDescFiveStudents");
        return studentRepository.getDescFiveStudents();
    }
}
