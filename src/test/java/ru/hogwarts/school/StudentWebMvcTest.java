package ru.hogwarts.school;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private FacultyController facultyController;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private AvatarService avatarService;

    @MockBean
    private AvatarController avatarController;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void createTest() throws Exception {
        Student student = new Student(null,"St1",20);

        JSONObject jsonStudent = new JSONObject();
        jsonStudent.put("name", "St1");
        jsonStudent.put("age", 20);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                    .post("/student")
                    .content(jsonStudent.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("St1"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void updateTest() throws Exception {
        Faculty faculty = new Faculty(null,"F1","red");
        Student student = new Student(1L, "St1", 20);
        student.setFaculty(faculty);

        JSONObject jsonStudent = new JSONObject();
        jsonStudent.put("name", "St1");
        jsonStudent.put("age", 18);
        jsonStudent.put("id", 2L);

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                    .put("/student/1")
                    .content(jsonStudent.toString())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void getTest() throws Exception {
        Student student = new Student(1L, "St1", 20);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("St1"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void deleteTest() throws Exception {
        Student student = new Student(1L, "St1", 20);

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("St1"))
                .andExpect(jsonPath("$.age").value(20));
    }

}
