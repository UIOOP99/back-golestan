package ir.ui.golestan.controller;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.CourseDate;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.DateRepository;
import ir.ui.golestan.exception.CourseNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController extends BaseController {

    private final CourseRepository repository;
    private final DateRepository dateRepository;

    public CourseController(GolestanConfiguration configuration, AuthorizationService authorizationService, CourseRepository repository, DateRepository dateRepository) {
        super(authorizationService);
        this.repository = repository;
        this.dateRepository = dateRepository;
    }

    //add new course
    @PostMapping("/courses")
    public Course newCourse(@RequestHeader("authorization") String token, @RequestBody Course newCourse) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);

        List<CourseDate> dates = dateRepository.saveAll(newCourse.getDates());
        newCourse.setDates(dates);

        return repository.save(newCourse);
    }

    //get a course by id
    @GetMapping("/courses")
    public Course one(@RequestParam int id) {
        return repository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    //get a list of all courses
    @GetMapping("/courses_all")
    public List<Course> all() {
        return repository.findAll();
    }

    //delete a course by id
    @DeleteMapping("/courses")
    public void deleteCourse(@RequestHeader("authorization") String token, @RequestParam int id) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);
        repository.deleteById(id);
    }

}
