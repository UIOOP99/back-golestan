package ir.ui.golestan.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.CourseDate;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.DateRepository;
import ir.ui.golestan.grpc.AuthGrpcClientService;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.UserRole;
import ir.ui.golestan.data.repository.UserRoleRepository;

@RestController
public class AdminController extends BaseController {

    private final UserRoleRepository userRole;
    private final CourseRepository courseRepository;
    private final DateRepository dateRepository;
    private final AuthGrpcClientService authGrpcClientService;

    public AdminController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                           UserRoleRepository userRole, CourseRepository courseRepository, DateRepository dateRepository, AuthGrpcClientService authGrpcClientService) {
        super(configuration, authorizationService);
        this.userRole = userRole;
        this.courseRepository = courseRepository;
        this.dateRepository = dateRepository;
        this.authGrpcClientService = authGrpcClientService;
    }

    @GetMapping("/admin/get_role")
    public UserRole getRole(int userId) {

        return userRole.getOne(userId);
    }

    @GetMapping("/admin/get_allProfessors")
    public List<Integer> findAllProfessorIdsList() {

        return userRole.findAllByRole(Role.PROFESSOR).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/get_allStudents")
    public List<Integer> findAllStudentIdsList() {

        return userRole.findAllByRole(Role.STUDENT).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
    }

    @PostMapping("/admin/add_user")
    public int createUser(RequestEntity<?> requestEntity, @RequestBody InputUser newUser) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);

        int userId = authGrpcClientService.signup(newUser);
        userRole.save(UserRole.builder().userId(userId).role(Role.valueOf(newUser.role)).build());
        return userId;
    }

    @PostMapping("/course/add_date")
    public CourseDate addDate(RequestEntity<?> requestEntity, int courseId, @RequestBody CourseDate date) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);

        CourseDate saved = dateRepository.save(date);
        Course course = courseRepository.getOne(courseId);
        List<CourseDate> dates = new ArrayList<>(course.getDates());
        dates.add(saved);
        course.setDates(dates);
        courseRepository.save(course);
        return saved;
    }

    @DeleteMapping("/course/delete_date")
    public void deleteDate(RequestEntity<?> requestEntity, int dateId) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);
        dateRepository.deleteById(dateId);
    }

    @Value
    @Builder
    @JsonDeserialize
    public static class InputUser {
        String firstname;

        String lastname;

        String username;

        String email;

        String password;

        String role;
    }

}
