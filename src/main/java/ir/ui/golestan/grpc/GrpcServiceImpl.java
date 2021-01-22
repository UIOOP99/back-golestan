package ir.ui.golestan.grpc;

import io.grpc.stub.StreamObserver;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.entity.UserRole;
import ir.ui.golestan.data.repository.CourseRepository;
import ir.ui.golestan.data.repository.ScoreRepository;
import ir.ui.golestan.data.repository.UserRepository;
import ir.ui.golestan.data.repository.UserRoleRepository;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase{

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ScoreRepository scoreRepository;

    public GrpcServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, CourseRepository courseRepository, ScoreRepository scoreRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
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
        Integer userId = request.getUserId();

        List<Course> courses = null; //TODO fetch accordingly based on the userId

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getPersonCoursesByTerm(TermIdRequest request, StreamObserver<CoursesResponse> responseObserver) {
        Integer termId = request.getTermId();

        List<Course> courses = null; //TODO fetch accordingly based on the termId

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getCoursesOfStudentOfCourseId(CourseIdRequest request, StreamObserver<CoursesResponse> responseObserver) {
        Integer courseId = request.getCourseId();

        List<Course> courses = null; //TODO fetch accordingly based on the courseId

        responseObserver.onNext(coursesToCoursesResponse(courses));
        responseObserver.onCompleted();
    }

    @Override
    public void getUserRole(UserIdRequest request, StreamObserver<RoleResponse> responseObserver) {
        UserRole role = userRoleRepository.findByUserId(request.getUserId());
        RoleResponse roleResponse = roleToRoleResponse(role);

        responseObserver.onNext(roleResponse);
        responseObserver.onCompleted();
    }

    private CoursesResponse coursesToCoursesResponse(List<Course> courses)
    {
        CoursesResponse.Builder coursesbuilder = CoursesResponse.newBuilder();
        int i = 0;
        for(Course course : courses)
        {
            coursesbuilder.setCourses(i++, courseToCourseResponse(course));
        }
        return coursesbuilder.build();
    }

    private CourseResponse courseToCourseResponse(Course course)
    {
        return CourseResponse.newBuilder()
                .setId(course.getId())
                .setSemesterId(course.getSemesterId())
                .setProfessorId(course.getProfessorId())
                .build();
    }

    private RoleResponse roleToRoleResponse(UserRole role)
    {
        return RoleResponse.newBuilder()
                .setUserId(role.getUserId())
                .setRole(role.getRole().toString())
                .build();
    }
}
