package ir.ui.golestan.authorization;

import ir.ui.golestan.GolestanConfiguration;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;

public class BaseController {

    private final GolestanConfiguration configuration;
    private final AuthorizationService authorizationService;

    public BaseController(GolestanConfiguration configuration, AuthorizationService authorizationService) {
        this.configuration = configuration;
        this.authorizationService = authorizationService;
    }

    protected AuthenticatedUser getAuthenticatedUser(RequestEntity<?> requestEntity) {
        String jwtToken = requestEntity.getHeaders().getFirst(configuration.getAuthHeader());
        return authorizationService.getAuthenticatedUser(jwtToken);
    }

    protected AuthenticatedUser getAuthenticatedUser(RequestEntity<?> requestEntity, Role requiredRole) {
        AuthenticatedUser user = getAuthenticatedUser(requestEntity);
        Assert.isTrue(user.getRole() == requiredRole, () -> "Unauthorized action, user=" + user + " , requiredRole=" + requiredRole);

        return user;
    }
}
