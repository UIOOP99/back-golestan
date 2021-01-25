package ir.ui.golestan.controller;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.data.repository.SemesterRepository;
import ir.ui.golestan.grpc.AuthGrpcClientService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProfessorController extends BaseController {

    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final AuthGrpcClientService authGrpcClientService;

    public ProfessorController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                               ScoreRepository scoreRepository, CourseRepository courseRepository, SemesterRepository semesterRepository, AuthGrpcClientService authGrpcClientService) {
        super(authorizationService);
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
        this.authGrpcClientService = authGrpcClientService;
    }

    @GetMapping("/professor/profile")
    public AuthenticatedUser profile(@RequestHeader("authorization") String token) {
        return getAuthenticatedUser(token, Role.PROFESSOR);
    }

    @PostMapping("/professor/set_score")
    public void setStudentScore(@RequestHeader("authorization") String token, @RequestPart int studentId, @RequestPart int courseId, @RequestPart double score) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.PROFESSOR);
        scoreRepository.save(Score.builder().studentId(studentId).courseId(courseId).score(score).build());
    }

    @GetMapping("/professor/get_courses")
    public List<Map<String, Object>> getAllCourses(@RequestHeader("authorization") String token) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.PROFESSOR);
        return courseRepository.findAllByProfessorId(user.getUserId()).stream()
                .map(course -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", course.getId());
                    map.put("name", course.getName());
                    map.put("dates", course.getDates());
                    map.put("units", course.getUnits());
                    map.put("semester_id", course.getSemesterId());
                    map.put("semester_name", semesterRepository.getOne(course.getSemesterId()).getName());
                    map.put("population", course.getStudentsIds().length);
                    return map;
                }).collect(Collectors.toList());
    }

    @GetMapping("/professor/get_semester_courses")
    public List<Map<String, Object>> getAllSemesterCourses(@RequestHeader("authorization") String token, int semesterId) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.PROFESSOR);
        return courseRepository.findAllByProfessorIdAndSemesterId(user.getUserId(), semesterId).stream()
                .map(course -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("course_id", course.getId());
                    map.put("name", course.getName());
                    map.put("dates", course.getDates());
                    map.put("units", course.getUnits());
                    map.put("semester_id", course.getSemesterId());
                    map.put("semester_name", semesterRepository.getOne(course.getSemesterId()).getName());
                    map.put("population", course.getStudentsIds().length);
                    return map;
                }).collect(Collectors.toList());
    }

    @GetMapping("/professor/get_course_students")
    public List<Map<String, Object>> getCourseStudents(int courseId) {
        List<Long> longs = new ArrayList<>();
        for (long l : courseRepository.getOne(courseId).getStudentsIds()) longs.add(l);
        return authGrpcClientService.getUserInfo(longs).stream()
                .map(u -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", u.getUserId());
                    map.put("first_name", u.getFirstName());
                    map.put("last_name", u.getLastName());
                    map.put("username", u.getUsername());

                    map.put("score", scoreRepository.findById(Score.ScoreId.of(u.getUserId(), courseId)).map(Score::getScore).orElse(0.0));
                    return map;
                }).collect(Collectors.toList());
    }
}
