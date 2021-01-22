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
import ir.ui.golestan.grpc.AuthGrpcClientService;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProfessorController extends BaseController {

    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final AuthGrpcClientService authGrpcClientService;

    public ProfessorController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                               ScoreRepository scoreRepository, CourseRepository courseRepository, AuthGrpcClientService authGrpcClientService) {
        super(configuration, authorizationService);
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.authGrpcClientService = authGrpcClientService;
    }

    @GetMapping("/professor/set_score")
    public void setStudentScore(RequestEntity<?> request, int studentId, int courseId, double score) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.PROFESSOR);
        scoreRepository.save(Score.builder().studentId(studentId).courseId(courseId).score(score).build());
    }

    @GetMapping("/professor/get_courses")
    public List<Course> getAllCourses(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.PROFESSOR);
        return courseRepository.findAllByProfessorId(user.getUserId());
   }

    @GetMapping("/professor/get_semester_courses")
    public List<Course> getAllSemesterCourses(RequestEntity<?> request, int semesterId) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.PROFESSOR);
        return courseRepository.findAllByProfessorIdAndSemesterId(user.getUserId(), semesterId);
    }

    @GetMapping("/professor/get_course_students")
    public List<Map<String, Object>> getCourseStudents(int courseId) {
        return Arrays.stream(courseRepository.getOne(courseId).getStudentsIds())
                .mapToObj(studentId -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("first_name", "ATAL");
                    map.put("last_name", "MATAL");
                    map.put("username", "TOTOLE");

                    map.put("score", scoreRepository.getOne(Score.ScoreId.of(studentId, courseId)).getScore());
                    return map;
                }).collect(Collectors.toList());
    }
}
