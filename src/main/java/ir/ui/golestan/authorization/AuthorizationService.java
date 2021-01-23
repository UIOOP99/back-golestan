package ir.ui.golestan.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.ECDSAKeyProvider;
import com.google.gson.Gson;
import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.data.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRoleRepository repository;
    private final GolestanConfiguration configuration;

    @SneakyThrows
    public AuthenticatedUser getAuthenticatedUser(String token) {
        Algorithm algorithm = Algorithm.ECDSA512(new ECDSAKeyProvider() {
            @Override
            @SneakyThrows
            public ECPublicKey getPublicKeyById(String keyId) {
                return new ECPublicKeyImpl(Base64.getDecoder().decode(configuration.getPublicKey().getBytes()));
            }

            @Override
            @SneakyThrows
            public ECPrivateKey getPrivateKey() {
                return new ECPrivateKeyImpl(Base64.getDecoder().decode(configuration.getPrivateKey().getBytes()));
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
                .role(user.getUserId() == 1 ? Role.ADMIN :
                        repository.findById(user.getUserId())
                                .orElseThrow(() -> new IllegalAccessException("Unauthorized user: " + user))
                                .getRole())
                .build();
    }
}
