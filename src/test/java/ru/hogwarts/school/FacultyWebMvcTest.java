package ru.hogwarts.school;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private AvatarService avatarService;

    @MockBean
    private AvatarController avatarController;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void createTest() throws Exception {
        Faculty faculty = new Faculty(null,"F1","red");

        JSONObject jsonFaculty = new JSONObject();
        jsonFaculty.put("name", "F1");
        jsonFaculty.put("color", "red");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(jsonFaculty.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("F1"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void updateTest() throws Exception {
        Faculty faculty = new Faculty(1L,"F1","red");

        JSONObject jsonFaculty = new JSONObject();
        jsonFaculty.put("name", "F1");
        jsonFaculty.put("color", "red");

        when(facultyRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/1")
                        .content(jsonFaculty.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(facultyRepository, times(1)).save(any());
    }

    @Test
    void getTest() throws Exception {
        Faculty faculty = new Faculty(1L, "F1", "red");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("F1"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void deleteTest() throws Exception {
        Faculty faculty = new Faculty(1L, "F1", "red");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("F1"))
                .andExpect(jsonPath("$.color").value("red"));
    }
}
