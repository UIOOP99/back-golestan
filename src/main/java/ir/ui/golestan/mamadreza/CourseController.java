package ir.ui.golestan.mamadreza;

import com.fasterxml.jackson.databind.ser.Serializers;
import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.Course;
import ir.ui.golestan.data.repository.CourseRepository;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController extends BaseController {

    private final CourseRepository repository;

    CourseController(GolestanConfiguration configuration, AuthorizationService authorizationService, CourseRepository repository) {
        super(configuration, authorizationService);
        this.repository = repository;
    }

    @PostMapping("/newcourse")
    Course newCourse (RequestEntity<?> requestEntity, @RequestBody Course newCourse) {
        getAuthenticatedUser(requestEntity, Role.ADMIN);
        return repository.save(newCourse);
    }

}
