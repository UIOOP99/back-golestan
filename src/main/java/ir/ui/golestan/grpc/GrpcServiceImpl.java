package ir.ui.golestan.grpc;

import io.grpc.stub.StreamObserver;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.UserRole;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.data.repository.UserRoleRepository;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {

    private final UserRoleRepository userRoleRepository;
    private final CourseRepository courseRepository;
    private final ScoreRepository scoreRepository;

    public GrpcServiceImpl(UserRoleRepository userRoleRepository, CourseRepository courseRepository, ScoreRepository scoreRepository) {
        this.userRoleRepository = userRoleRepository;
        this.courseRepository = courseRepository;
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void getAllCourses(Empty request, StreamObserver<CoursesResponse> responseObserver) {
        List<Course> courses = courseRepository.findAll();

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonCourses(UserIdRequest request, StreamObserver<CoursesResponse> responseObserver) {
        long userId = request.getUserId();

        List<Course> courses = userRoleRepository.getOne(userId).getRole() == Role.PROFESSOR ?
                courseRepository.findAllByProfessorId(userId) :
                courseRepository.findAll().stream()
                        .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == userId))
                        .collect(Collectors.toList());

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonCoursesByTerm(PersonTermRequest request, StreamObserver<CoursesResponse> responseObserver) {
        long userId = request.getUserId();
        int termId = request.getTermId();

        List<Course> courses = userRoleRepository.getOne(userId).getRole() == Role.PROFESSOR ?
                courseRepository.findAllByProfessorIdAndSemesterId(userId, termId) :
                courseRepository.findAllBySemesterId(termId).stream()
                        .filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> id == userId))
                        .collect(Collectors.toList());

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getCoursesOfStudentOfCourseId(CourseIdRequest request, StreamObserver<CoursesResponse> responseObserver) {
        Integer courseId = request.getCourseId();

        List<Course> all = courseRepository.findAll();
        List<Course> courses = Arrays.stream(courseRepository.getOne(courseId).getStudentsIds())
                .mapToObj(studentId -> all.stream().filter(c -> Arrays.stream(c.getStudentsIds()).anyMatch(id -> studentId == id)))
                .flatMap(courseStream -> courseStream)
                .collect(Collectors.toList());

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getUserRole(UserIdRequest request, StreamObserver<RoleResponse> responseObserver) {
        UserRole role = userRoleRepository.getOne(request.getUserId());
        RoleResponse roleResponse = roleToRoleResponse(role);

        responseObserver.onNext(roleResponse);
        responseObserver.onCompleted();
    }

    private CoursesResponse coursesToCoursesResponse(List<Course> courses) {
        CoursesResponse.Builder coursesbuilder = CoursesResponse.newBuilder();
        int i = 0;
        for (Course course : courses) {
            coursesbuilder.setCourses(i++, courseToCourseResponse(course));
        }
        return coursesbuilder.build();
    }

    private CourseResponse courseToCourseResponse(Course course) {
        return CourseResponse.newBuilder()
                .setId(course.getId())
                .setSemesterId(course.getSemesterId())
                .setProfessorId(course.getProfessorId())
                .build();
    }

    private RoleResponse roleToRoleResponse(UserRole role) {
        return RoleResponse.newBuilder()
                .setUserId(role.getUserId())
                .setRole(role.getRole().toString())
                .build();
    }
}
