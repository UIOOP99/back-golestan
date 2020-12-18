package ir.ui.golestan.restservice;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.authorization.BaseController;
import ir.ui.golestan.authorization.Role;
import ir.ui.golestan.data.repository.UserRepository;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController {

    private final UserRepository userRepository;

    public UserController(GolestanConfiguration configuration, AuthorizationService authorizationService, UserRepository userRepository) {
        super(configuration, authorizationService);
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public AuthenticatedUser createUser (RequestEntity<?> requestEntity, @RequestBody AuthenticatedUser newUser)
    {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity, Role.ADMIN);
        return userRepository.save(newUser);
    }


}
