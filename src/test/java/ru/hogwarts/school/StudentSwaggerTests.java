package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentSwaggerTests {
    @LocalServerPort
    private int port;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentController studentController;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void clear() {
        studentRepository.deleteAll();
    }

    @Test
    void create() {
        Student student = new Student(null, "st1", 12);
        student.setFaculty(null);

        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class))
                .isNotNull();
        Student actual = studentRepository.findById(1L).orElseThrow();
        Assertions.assertThat(actual).isEqualToIgnoringGivenFields(student,"id");
    }

    @Test
    void update() {
        Student student = new Student(null, "st1", 12);
        student = studentRepository.save(student);

        Student student1 = new Student(null, "st2", 20);
        student1.setId(student.getId());

        System.out.println(student1.hashCode());

        restTemplate.put("http://localhost:" + port + "/student/"+student1.getId(), student1);
        Student actual = studentRepository.findById(student1.getId()).orElseThrow();
        System.out.println(actual);
        Assertions.assertThat(actual).isEqualTo(student1);
    }

    @Test
    void get() {
        Student expected = new Student(null, "st1", 12);
        expected = studentRepository.save(expected);
        Student actual = studentRepository.findById(expected.getId()).orElseThrow();
        Assertions.assertThat(actual).isEqualTo(expected);
        Student actual1 = restTemplate.getForObject("http://localhost:" + port + "/student/"+expected.getId(), Student.class);
        Assertions.assertThat(actual1).isEqualTo(expected);
    }


    @Test
    void delete() {
        Student expected = new Student(null, "st1", 12);
        expected = studentRepository.save(expected);
        Student actual = studentRepository.findById(expected.getId()).orElseThrow();
        Assertions.assertThat(actual).isEqualTo(expected);
        restTemplate.delete("http://localhost:" + port + "/student/"+expected.getId());
        Assertions.assertThat(studentRepository.findById(expected.getId())).isEmpty();
    }



}
