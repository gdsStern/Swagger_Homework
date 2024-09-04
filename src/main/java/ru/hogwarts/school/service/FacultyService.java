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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Вызван метод createFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long facultyId) {
        logger.info("Вызван метод getFacultyById");
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
                        logger.error("Не найден факультет с id " + facultyId);
                        return new FacultyNotFoundException();
                });
    }

    public void updateFaculty(Long facultyId, Faculty faculty) {
        logger.info("Вызван метод updateFaculty");
        Faculty oldFaculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
            logger.error("Не найден факультет с id " + facultyId);
            return new FacultyNotFoundException();
        });
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }

    public Faculty deleteFaculty(Long facultyId) {
        logger.info("Вызван метод deleteFaculty");
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> {
            logger.error("Не найден факультет с id " + facultyId);
            return new FacultyNotFoundException();
        });
        facultyRepository.delete(faculty);
        return faculty;
    }

    public List<Faculty> filterByColor(String color) {
        logger.info("Вызван метод filterByColor");
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> filterByColorOrName(String colorOrName) {
        logger.info("Вызван метод filterByColorOrName");
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }

    public List<Student> findAllByFacultyId(Long id) {
        logger.info("Вызван метод findAllByFacultyId");
        return studentRepository.findAllByFaculty_Id(id);
    }
}
