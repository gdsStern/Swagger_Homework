package ru.hogwarts.school.controller;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        System.out.println(faculty.toString() + " controller");
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Faculty faculty) {
        facultyService.updateFaculty(id, faculty);
    }

    @GetMapping("/{id}")
    public Faculty get(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    @DeleteMapping("/{id}")
    public Faculty remove(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping(params = "color")
    public List<Faculty> filterByColor(@RequestParam String color) {
        return facultyService.filterByColor(color);
    }

    @GetMapping(params = "colorOrName")
    public List<Faculty> filterByColorOrName(@RequestParam String colorOrName) {
        return facultyService.filterByColorOrName(colorOrName);
    }

    @GetMapping("/{id}/students")
    public List<Student> findAllByFacultyId(@PathVariable Long id) {
        return facultyService.findAllByFacultyId(id);
    }

    @GetMapping("/longerName")
    public String getLongerNameFaculty() {
        return facultyService.getLongerNameFaculty();
    }

    @GetMapping("/int")
    public int getInt() {
        return facultyService.getInt();
    }
}

