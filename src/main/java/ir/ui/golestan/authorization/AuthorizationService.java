package ir.ui.golestan.authorization;

import ir.ui.golestan.GolestanConfiguration;
import ir.ui.golestan.data.repository.UserRoleRepository;
import ir.ui.golestan.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.nio.file.AccessDeniedException;
import java.security.AccessControlException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRoleRepository repository;
    private final GolestanConfiguration configuration;

    public AuthenticatedUser getAuthenticatedUser(String token) {
        AuthenticatedUser user;
        try {
            user = decodeJwt(token);
        } catch (Exception e) {
            throw new AuthenticationException();
        }

        return user.toBuilder()
                .role(user.getUserId() == 1 ? Role.ADMIN : repository.getOne(user.getUserId()).getRole())
                .build();
    }

    public AuthenticatedUser decodeJwt(String jwt) throws Exception {
        String publicKeyPEM = new String(Base64.getDecoder().decode(configuration.getPublicKey()));

        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] publicKeyBytes = base64Decoder.decodeBuffer(publicKeyPEM);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(publicKey)
                .build();

        JwtClaims jwtDecoded = jwtConsumer.processToClaims(jwt);
        Map<String, Object> map = jwtDecoded.getClaimsMap();
        return AuthenticatedUser.builder()
                .userId((Long) map.get("user_id"))
                .username((String) map.get("username"))
                .firstName((String) map.get("first_name"))
                .lastName((String) map.get("last_name"))
                .email((String) map.get("email"))
                .build();
    }

}
