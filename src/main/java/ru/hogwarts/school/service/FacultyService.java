package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException());
    }

    public void updateFaculty(Long facultyId, Faculty faculty) {
        Faculty oldFaculty = facultyRepository.findById(facultyId).
                orElseThrow(FacultyNotFoundException::new);
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId).
                orElseThrow(FacultyNotFoundException::new);
        facultyRepository.delete(faculty);
        return faculty;
    }

    public List<Faculty> filterByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> filterByColorOrName(String colorOrName) {
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }

    public List<Student> findAllByFacultyId(Long id) {
        return studentRepository.findAllByFaculty_Id(id);
    }
}
