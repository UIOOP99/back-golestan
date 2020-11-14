package ir.ui.golestan.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.ECDSAKeyProvider;
import com.google.gson.Gson;
import ir.ui.golestan.data.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRoleRepository repository;

    @SneakyThrows
    public AuthenticatedUser getAuthenticatedUser(String token) {
        Algorithm algorithm = Algorithm.ECDSA512(new ECDSAKeyProvider() {
            @Override
            public ECPublicKey getPublicKeyById(String keyId) {
                return null;
            }

            @Override
            public ECPrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public String getPrivateKeyId() {
                return null;
            }
        });
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();
        DecodedJWT jwt = verifier.verify(token);

        AuthenticatedUser user = new Gson().fromJson(jwt.getPayload(), AuthenticatedUser.class);

        return user.toBuilder()
                .role(repository.findById(user.getUserId())
                        .orElseThrow(() -> new IllegalAccessException("Unauthorized user: " + user))
                        .getRole())
                .build();
    }
}
