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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
        super(authorizationService);
        this.courseRepository = courseRepository;
        this.scoreRepository = scoreRepository;
        this.dateRepository = dateRepository;
        this.semesterRepository = semesterRepository;
    }

    @GetMapping("/student/profile")
    public AuthenticatedUser profile(@RequestHeader("authorization") String token) {
        return getAuthenticatedUser(token, Role.PROFESSOR);
    }

    @GetMapping("/student/get_scores")
    public List<Score> getStudentScores(@RequestHeader("authorization") String token) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.STUDENT);
        return scoreRepository.findAllByStudentId(user.getUserId());
    }

    @GetMapping("/student/get_courses")
    public List<Course> getAllStudentCourses(@RequestHeader("authorization") String token) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.STUDENT);
        return courseRepository.findAll().stream()
                .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == user.getUserId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/student/get_semester_courses")
    public List<Course> getAllSemesterStudentCourses(@RequestHeader("authorization") String token, int semesterId) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.STUDENT);
        return courseRepository.findAllBySemesterId(semesterId).stream()
                .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == user.getUserId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/student/get_schedule")
    public List<CourseDate> getStudentCourseDates(@RequestHeader("authorization") String token) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.STUDENT);
        return getAllStudentCourses(token).stream()
                .flatMap(c -> c.getDates().stream())
                .collect(Collectors.toList());
    }

    @GetMapping("/student/get_semester_info")
    public List<Map<String, Object>> getStudentSemesterInfo(@RequestHeader("authorization") String token) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.STUDENT);
        List<Map<String, Object>> list = new ArrayList<>();
        semesterRepository.findAll().stream()
                .filter(semester -> getAllStudentCourses(token).stream().anyMatch(c -> c.getSemesterId() == semester.getId()))
                .forEach(semester -> {
                    Map<String, Object> map = new HashMap<>();
                    String status = "PLACEHOOLDER";
                    int unit = 0;
                    int sum = 0;
                    List<Course> courses = courseRepository.findAllBySemesterId(semester.getId()).stream()
                            .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == user.getUserId()))
                            .collect(Collectors.toList());
                    for (Course crs : courses) {
                        Optional<Score> score = scoreRepository.findById(Score.ScoreId.of(user.getUserId(), crs.getId()));
                        if (score.isPresent()) {
                            sum += score.get().getScore();
                        }
                        unit += crs.getUnits();
                    }
                    map.put("semester_id", semester.getId());
                    map.put("semester_name", semester.getName());
                    map.put("status", status);
                    map.put("unit", unit);
                    int average = unit == 0 ? 0 : sum / unit;
                    map.put("average", average);
                    list.add(map);
                });
        return list;
    }

}
