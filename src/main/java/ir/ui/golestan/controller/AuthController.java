package ir.ui.golestan.controller;

import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.CourseDate;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.data.repository.SemesterRepository;
import ir.ui.golestan.data.repository.UserRoleRepository;
import ir.ui.golestan.exception.AuthenticationException;
import ir.ui.golestan.grpc.AuthGrpcClientService;
import ir.ui.golestan.grpc.AuthPairToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthorizationService authorizationService;
    private final AuthGrpcClientService authGrpcClientService;
    private final UserRoleRepository userRoleRepository;
    private final CourseRepository courseRepository;
    private final ScoreRepository scoreRepository;
    private final SemesterRepository semesterRepository;

    private final AdminController adminController;
    private final CourseController courseController;
    private final ProfessorController professorController;
    private final StudentController studentController;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestPart String username, @RequestPart String password) {
        try {
            Map<String, Object> map = new HashMap<>();
            AuthPairToken token = authGrpcClientService.login(username, password);

            AuthenticatedUser user = authorizationService.getAuthenticatedUser(token.getAccess());

            map.put("user_id", user.getUserId());
            map.put("role", user.getRole());
            map.put("token", token.getAccess());

            if (userRoleRepository.count() == 0 && user.getRole() == Role.ADMIN) {
                generateFakeData(token.getAccess());
            }

            return map;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handle() {
        return ResponseEntity.status(400).body("UNAUTHORIZED");
    }

    private void generateFakeData(String token) {
        BiFunction<String, Integer, AdminController.InputUser.InputUserBuilder> userBuilderFunction = (role, num) -> AdminController.InputUser.builder()
                .firstname("fn:" + role + num)
                .lastname("ln:" + role + num)
                .password(String.valueOf(num))
                .email("abc@abc.com")
                .username("u" + num);

        List<Long> professorIds = new ArrayList<>();
        List<Long> studentIds = new ArrayList<>();

        IntStream.range(0, 10).forEach(i -> {
            professorIds.add(adminController.createUser(token, userBuilderFunction.apply("professor", i).role("PROFESSOR").build()).getUserId());
        });

        IntStream.range(10, 100).forEach(i -> {
            studentIds.add(adminController.createUser(token, userBuilderFunction.apply("student", i).role("STUDENT").build()).getUserId());
        });

        IntStream.range(0, 10).forEach(i -> {
            adminController.addSemester(token, "semester:" + i);
        });

        Random random = new Random();
        IntStream.range(0, 100).forEach(i -> {
            long[] sids = new long[20];
            for (int k = 0; k < sids.length; k++) {
                sids[k] = studentIds.get(random.nextInt(studentIds.size()));
            }

            courseController.newCourse(token, Course.builder()
                    .semesterId(random.nextInt(10))
                    .dates(IntStream.range(0, 4)
                            .mapToObj(j -> CourseDate.builder()
                                    .day("day:" + i + "" + j)
                                    .start("start:" + j)
                                    .end("end:" + j)
                                    .build())
                            .collect(Collectors.toList()))
                    .name("course:" + i)
                    .professorId(professorIds.get(random.nextInt(professorIds.size())))
                    .units(random.nextInt(3) + 1)
                    .studentsIds(sids)
                    .build());
        });
    }

}
