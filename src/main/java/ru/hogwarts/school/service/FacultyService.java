package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacultyService {
    private Map<Long, Faculty> facultyMap = new HashMap<>();
    private Long id = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(id++);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFacultyById(Long facultyId) {
        if (!facultyMap.containsKey(facultyId)) {
            throw new FacultyNotFoundException();
        }
        return facultyMap.get(facultyId);
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        if (!facultyMap.containsKey(facultyId)) {
            throw new FacultyNotFoundException();
        }
        Faculty oldFaculty = facultyMap.get(facultyId);
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        return faculty;
    }

    public Faculty deleteFaculty(Long facultyId) {
        if (!facultyMap.containsKey(facultyId)) {
            throw new FacultyNotFoundException();
        }
        return facultyMap.remove(facultyId);
    }
    public List<Faculty> filterByColor(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
    }
}
