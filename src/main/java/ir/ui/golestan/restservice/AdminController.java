package ir.ui.golestan.restservice;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.entity.UserRole;
import ir.ui.golestan.data.repository.UserRoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController extends BaseController {

    private final UserRoleRepository userRole;

    public AdminController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                           UserRoleRepository userRole) {
        super(configuration, authorizationService);
        this.userRole = userRole;

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


}
