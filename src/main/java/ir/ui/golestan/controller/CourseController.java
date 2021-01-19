package ir.ui.golestan.controller;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.exception.CourseNotFoundException;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.repository.CourseRepository;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController extends BaseController {

    private final CourseRepository repository;

    CourseController(GolestanConfiguration configuration, AuthorizationService authorizationService, CourseRepository repository) {
        super(configuration, authorizationService);
        this.repository = repository;
    }

    //add new course
    @PostMapping("/courses")
    Course newCourse (RequestEntity<?> requestEntity, @RequestBody Course newCourse) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);
        return repository.save(newCourse);
    }

    //edit a course specifications
    @PutMapping("/courses/{id}")
    Course replaceCourse (RequestEntity<?> requestEntity, @RequestBody Course newCourse, @PathVariable int id) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);

        return repository.findById(id)
                .map(course -> {
                    course.setId(newCourse.getId());
                    course.setProfessorId(newCourse.getProfessorId());
                    course.setStudentsIds(newCourse.getStudentsIds());
                    return repository.save(course);
                })
                .orElseGet(() -> {
                    newCourse.setId(id);
                    return repository.save(newCourse);
                });
    }

    //get a course by id
    @GetMapping("/courses/{id}")
    Course one(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    //get a list of all courses
    @GetMapping("/courses")
    List<Course> all() {
        return repository.findAll();
    }

    //delete a course by id
    @DeleteMapping("/courses/{id}")
    void deleteCourse (RequestEntity<?> requestEntity, @RequestBody Course delCourse) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);
        repository.delete(delCourse);
    }

}
