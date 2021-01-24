package ir.ui.golestan.controller;

import auth.AuthOuterClass;
import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.CourseDate;
import ir.ui.golestan.data.entity.Semester;
import ir.ui.golestan.data.entity.UserRole;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.DateRepository;
import ir.ui.golestan.data.repository.SemesterRepository;
import ir.ui.golestan.data.repository.UserRoleRepository;
import ir.ui.golestan.grpc.AuthGrpcClientService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AdminController extends BaseController {

    private final UserRoleRepository userRole;
    private final CourseRepository courseRepository;
    private final DateRepository dateRepository;
    private final SemesterRepository semesterRepository;
    private final AuthGrpcClientService authGrpcClientService;

    public AdminController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                           UserRoleRepository userRole, CourseRepository courseRepository, DateRepository dateRepository, SemesterRepository semesterRepository, AuthGrpcClientService authGrpcClientService) {
        super(authorizationService);
        this.userRole = userRole;
        this.courseRepository = courseRepository;
        this.dateRepository = dateRepository;
        this.semesterRepository = semesterRepository;
        this.authGrpcClientService = authGrpcClientService;
    }

    @GetMapping("/admin/get_role")
    public UserRole getRole(long userId) {

        return userRole.getOne(userId);
    }

    @GetMapping("/admin/get_allProfessors")
    public List<Map<String, Object>> findAllProfessorIdsList() {
        return getUsersInfo(userRole.findAllByRole(Role.PROFESSOR).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList()));
    }

    @GetMapping("/admin/get_allStudents")
    public List<Map<String, Object>> findAllStudentIdsList() {
        return getUsersInfo(userRole.findAllByRole(Role.STUDENT).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList()));
    }

    private List<Map<String, Object>> getUsersInfo(List<Long> usersId) {
        return usersId.stream()
                .map(userId -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("user_id", userId);
                    map.put("first_name", "fn:" + userId);
                    map.put("last_name", "ln:" + userId);
                    return map;
                }).collect(Collectors.toList());
    }

    @PostMapping("/admin/add_user")
    public AuthenticatedUser createUser(@RequestHeader("authorization") String token, @RequestBody InputUser newUser) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);

        AuthenticatedUser authenticatedUser = authGrpcClientService.signup(token, newUser);
        userRole.save(UserRole.builder().userId(authenticatedUser.getUserId()).role(Role.valueOf(newUser.role)).build());
        return authenticatedUser.toBuilder().role(Role.valueOf(newUser.role)).build();
    }

    @PutMapping("/admin/add_semester")
    public int addSemester(@RequestHeader("authorization") String token, @RequestPart String name) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);
        return semesterRepository.save(Semester.builder().name(name).build()).getId();
    }

    @DeleteMapping("/admin/delete_semester")
    public void deleteSemester(@RequestHeader("authorization") String token, @RequestParam int id) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);
        semesterRepository.deleteById(id);
    }

    @PostMapping("/course/add_date")
    public CourseDate addDate(@RequestHeader("authorization") String token, @RequestParam int courseId, @RequestBody CourseDate date) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);

        CourseDate saved = dateRepository.save(date);
        Course course = courseRepository.getOne(courseId);
        List<CourseDate> dates = new ArrayList<>(course.getDates());
        dates.add(saved);
        course.setDates(dates);
        courseRepository.save(course);
        return saved;
    }

    @DeleteMapping("/course/delete_date")
    public void deleteDate(@RequestHeader("authorization") String token, @RequestParam int dateId) {
        AuthenticatedUser user = getAuthenticatedUser(token, Role.ADMIN);
        dateRepository.deleteById(dateId);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputUser {
        String firstname;

        String lastname;

        String username;

        String email;

        String password;

        String role;
    }

}
