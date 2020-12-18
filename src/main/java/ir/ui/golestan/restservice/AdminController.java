package ir.ui.golestan.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    public AdminController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                           UserRoleRepository userRole) {
        super(configuration, authorizationService);
        this.userRole = userRole;
    }

    @GetMapping("/admin/get_role")
    public UserRole getRole(int userId) {
        return userRole.findByUserId(userId);
    }
}
