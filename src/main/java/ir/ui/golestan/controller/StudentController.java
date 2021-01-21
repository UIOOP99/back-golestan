package ir.ui.golestan.controller;

import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.Score;
import ir.ui.golestan.data.entity.Semester;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.SemesterRepository;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.repository.ScoreRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController extends BaseController {

    private final ScoreRepository scoreRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;

    public StudentController(GolestanConfiguration configuration, AuthorizationService authorizationService, ScoreRepository scoreRepository, CourseRepository courseRepository, SemesterRepository semesterRepository) {
        super(configuration, authorizationService);
        this.scoreRepository = scoreRepository;
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
    }

    @GetMapping("/student/get_scores")
    public List<Score> getStudentScores(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        return scoreRepository.findAllByStudentId(user.getUserId());
    }

    @GetMapping("/student/get_semesters_info")
    public List<Object[]> getStudentSemesters(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        List<Object[]> list = new ArrayList<Object[]>();
        int studentId = user.getUserId();
        int radif = 1;
        for (Semester semester:
             semesterRepository.findAllByStudentId(studentId)) {
            Object[] arr = new Object[5];
            arr[0] = radif;
            arr[1] = semester.getId();
            arr[2] = semester.getStatus();
            arr[3] = semester.getUnits();
            arr[4] = semester.getAverage();
            list.add(arr);
            radif++;
        }
        return list;
    }

    @GetMapping("/student/get_semester_courses")
    public List<Object[]> getStudentCourses(RequestEntity<?> request) {
        AuthenticatedUser user = getAuthenticatedUser(request, Role.STUDENT);
        List<Object[]> list = new ArrayList<Object[]>();
        int studentId = user.getUserId();
        int radif = 1;
        for (Course course:
             courseRepository.findAllByStudentId(studentId)) {
            Object[] arr = new Object[3];
            arr[0] = radif;
            arr[1] = course.getName();
            arr[2] = course.getCourseUnits();
            list.add(arr);
            radif++;
        }
        return list;
    }

}
