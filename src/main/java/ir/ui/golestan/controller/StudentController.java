package ir.ui.golestan.controller;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.CourseDate;
import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.entity.Semester;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.DateRepository;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.data.repository.SemesterRepository;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class StudentController extends BaseController {

    private final CourseRepository courseRepository;
    private final ScoreRepository scoreRepository;
    private final DateRepository dateRepository;
    private final SemesterRepository semesterRepository;

    public StudentController(GolestanConfiguration configuration, AuthorizationService authorizationService, CourseRepository courseRepository, ScoreRepository scoreRepository, DateRepository dateRepository, SemesterRepository semesterRepository) {
        super(configuration, authorizationService);
        this.courseRepository = courseRepository;
        this.scoreRepository = scoreRepository;
        this.dateRepository = dateRepository;
        this.semesterRepository = semesterRepository;
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
        return getAllStudentCourses(request).stream()
                .flatMap(c -> c.getDates().stream())
                .collect(Collectors.toList());
    }

    @GetMapping("/student/get_semester_info")
    public List<Map<String, Object>> getStudentSemesterInfo(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        List<Map<String, Object>> list = new ArrayList<>();
        semesterRepository.findAll().stream()
                .map(Semester::getId)
                .filter(id -> getAllStudentCourses(request).stream().anyMatch(c -> c.getSemesterId() == id))
                .forEach(semesterId -> {
                    Map<String, Object> map = new HashMap<>();
                    String status = "PLACEHOOLDER";
                    int unit = 0;
                    int sum = 0;
                    List<Course> courses = courseRepository.findAllBySemesterId(semesterId).stream()
                            .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == user.getUserId()))
                            .collect(Collectors.toList());
                    for (Course crs : courses) {
                        Score score = scoreRepository.getOne(Score.ScoreId.of(user.getUserId(), crs.getId()));
                        sum += score.getScore();
                        unit += crs.getUnits();
                    }
                    map.put("status", status);
                    map.put("unit", unit);
                    int average = sum / unit;
                    map.put("average", average);
                    list.add(map);
                });
        return list;
    }

}
