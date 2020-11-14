package ir.ui.golestan.authorization;

import ir.ui.golestan.GolestanConfiguration;
import org.springframework.http.RequestEntity;

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
}
