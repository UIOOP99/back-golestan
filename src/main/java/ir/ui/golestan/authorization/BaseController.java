package ir.ui.golestan.authorization;

import ir.ui.golestan.GolestanConfiguration;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    private final AuthorizationService authorizationService;

    public BaseController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    protected AuthenticatedUser getAuthenticatedUser(String token, Role requiredRole) {
        AuthenticatedUser user = authorizationService.getAuthenticatedUser(token);
        Assert.isTrue(user.getRole() == requiredRole, () -> "Unauthorized action, user=" + user + " , requiredRole=" + requiredRole);

        return user;
    }

}
