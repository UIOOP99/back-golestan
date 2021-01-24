package ir.ui.golestan.authorization;

import ir.ui.golestan.exception.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
