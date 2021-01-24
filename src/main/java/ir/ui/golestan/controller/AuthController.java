package ir.ui.golestan.controller;

import auth.AuthOuterClass;
import ir.ui.golestan.authorization.AuthenticatedUser;
import ir.ui.golestan.authorization.AuthorizationService;
import ir.ui.golestan.grpc.AuthGrpcClientService;
import ir.ui.golestan.grpc.AuthPairToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthorizationService authorizationService;
    private final AuthGrpcClientService authGrpcClientService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestPart String username, @RequestPart String password) {
        Map<String, Object> map = new HashMap<>();
        AuthPairToken token = authGrpcClientService.login(username, password);

        AuthenticatedUser user = authorizationService.getAuthenticatedUser(token.getAccess());

        map.put("user_id", user.getUserId());
        map.put("role", user.getRole());
        map.put("token", token.getAccess());
        return map;
    }
}
