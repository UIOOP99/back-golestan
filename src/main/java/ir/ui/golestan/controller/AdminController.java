package ir.ui.golestan.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ir.ui.golestan.data.entity.Course;
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

    public AdminController(GolestanConfiguration configuration, AuthorizationService authorizationService,
                           UserRoleRepository userRole) {
        super(configuration, authorizationService);
        this.userRole = userRole;
    }

    @GetMapping("/admin/get_role")
    public UserRole getRole(int userId) {
        return userRole.findByUserId(userId);
    }


    @PostMapping("/admin/add_user")
    public int createUser(RequestEntity<?> requestEntity, @RequestBody InputUser newUser) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);

        int userId = 0; // TODO Send newUser to auth with grpc and get user
        userRole.save(UserRole.builder().userId(userId).role(Role.valueOf(newUser.role)).build());
        return userId;
    }

    @Value
    @Builder
    @JsonDeserialize
    public static class InputUser {
        String firstname;

        String lastname;

        String email;

        String password;

        String role;
    }

}
