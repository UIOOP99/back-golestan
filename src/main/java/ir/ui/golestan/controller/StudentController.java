package ir.ui.golestan.controller;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.ScoreRepository;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StudentController extends BaseController {

    private final CourseRepository courseRepository;
    private final ScoreRepository scoreRepository;

    public StudentController(GolestanConfiguration configuration, AuthorizationService authorizationService, CourseRepository courseRepository, ScoreRepository scoreRepository, DateRepository dateRepository) {
        super(configuration, authorizationService);
        this.courseRepository = courseRepository;
        this.scoreRepository = scoreRepository;
        this.dateRepository = dateRepository;
    }

    @GetMapping("/student/get_scores")
    public List<Score> getStudentScores(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        return scoreRepository.findAllByStudentId(user.getUserId());
    }

    @GetMapping("/student/get_courses")
    public List<Course> getAllStudentCourses(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        return courseRepository.findAll().stream()
                .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == user.getUserId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/student/get_semester_courses")
    public List<Course> getAllSemesterStudentCourses(RequestEntity<?> request, int semesterId) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        return courseRepository.findAllBySemesterId(semesterId).stream()
                .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == user.getUserId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/student/get_schedule")
    public List<CourseDate> getStudentCourseDates(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        return dateRepository.findAll().stream()
        .filter(c -> Arrays.stream(c.getAllStudentCourses()).anyMatch(id -> id == user.getUserId()))
        .collect(Collectors.toList());
    }

}
