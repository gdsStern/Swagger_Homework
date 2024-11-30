package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void clear() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();

    }

    @Test
    void create() {
        Faculty faculty = new Faculty(null, "f1", "red");

//        Assertions
//                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class))
//                .isNotNull();

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Assertions.assertThat(facultyResponseEntity.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
    }

    @Test
    void update() {
        Faculty faculty = new Faculty(null, "st1", "red");
        faculty = facultyRepository.save(faculty);

        Faculty faculty1 = new Faculty(null, "st2", "red");
        faculty1.setId(faculty.getId());

        restTemplate.put("http://localhost:" + port + "/faculty/"+faculty1.getId(), faculty1);
        Faculty actual = facultyRepository.findById(faculty1.getId()).orElseThrow();
        System.out.println(actual);
        Assertions.assertThat(actual).isEqualTo(faculty1);
    }

    @Test
    void updateNegative() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("http://localhost:" + port + "/faculty/0", HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void get() {
        Faculty expected = new Faculty(null, "st1", "red");
        expected = facultyRepository.save(expected);
        Faculty actual = facultyRepository.findById(expected.getId()).orElseThrow();
        Assertions.assertThat(actual).isEqualTo(expected);
        Faculty actual1 = restTemplate.getForObject("http://localhost:" + port + "/faculty/"+expected.getId(), Faculty.class);
        Assertions.assertThat(actual1).isEqualTo(expected);
    }

    @Test
    void remove() {
        Faculty expected = new Faculty(null, "st1", "red");
        expected = facultyRepository.save(expected);
        Faculty actual = facultyRepository.findById(expected.getId()).orElseThrow();
        Assertions.assertThat(actual).isEqualTo(expected);
        restTemplate.delete("http://localhost:" + port + "/faculty/"+expected.getId());
        Assertions.assertThat(facultyRepository.findById(expected.getId())).isEmpty();
    }

    @Test
    void removeNegative() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("http://localhost:" + port + "/faculty/0", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void filterByColor() {
        Faculty f1 = new Faculty(null, "f1", "red");
        Faculty f2 = new Faculty(null, "f2", "blue");
        Faculty f3 = new Faculty(null, "f3", "red");
        f1 = facultyRepository.save(f1);
        f2 = facultyRepository.save(f2);
        f3 = facultyRepository.save(f3);

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(f1);
//        faculties.add(f2);
        faculties.add(f3);


        ResponseEntity<List<Faculty>> facultyList =
                restTemplate.exchange("http://localhost:" + port + "/faculty?color=red",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        Assertions.assertThat(facultyList.getBody()).isEqualTo(faculties);
    }

    @Test
    void filterByColorOrName() {
        Faculty f1 = new Faculty(null, "f1", "red");
        Faculty f2 = new Faculty(null, "f2", "blue");
        Faculty f3 = new Faculty(null, "f3", "red");
        f1 = facultyRepository.save(f1);
        f2 = facultyRepository.save(f2);
        f3 = facultyRepository.save(f3);

        List<Faculty> facultyName = new ArrayList<>();
        facultyName.add(f2);
        List<Faculty> facultyColor = new ArrayList<>();
        facultyColor.add(f2);

        ResponseEntity<List<Faculty>> facultyListName =
                restTemplate.exchange("http://localhost:" + port + "/faculty?colorOrName=f2",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        ResponseEntity<List<Faculty>> facultyListColor =
                restTemplate.exchange("http://localhost:" + port + "/faculty?colorOrName=blue",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertThat(facultyListName.getBody()).isEqualTo(facultyName);
        Assertions.assertThat(facultyListColor.getBody()).isEqualTo(facultyColor);

    }

    @Test
    void findAllStudentsByFacultyId() {
        Faculty faculty = new Faculty(null, "f1", "red");
        Faculty faculty1 = new Faculty(null, "f1", "red");
        faculty = facultyRepository.save(faculty);
        faculty1 = facultyRepository.save(faculty1);

        Student s1 = new Student(null, "s1", 12);
        s1.setFaculty(faculty);
        Student s2 = new Student(null, "s2", 13);
        s2.setFaculty(faculty1);
        Student s3 = new Student(null, "s3", 14);
        s3.setFaculty(faculty);

        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);

        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s3);

        ResponseEntity<List<Student>> studentList =
                restTemplate.exchange("http://localhost:" + port + "/faculty/"+faculty.getId()+"/students",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertThat(studentList.getBody()).isEqualTo(students);
    }
}