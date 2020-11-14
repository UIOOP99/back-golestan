package ir.ui.golestan.authorization;

import com.auth0.jwt.interfaces.ECDSAKeyProvider;
import ir.ui.golestan.GolestanConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Component
@RequiredArgsConstructor
public class GolestanECDSAKeyProvider implements ECDSAKeyProvider {

    private final GolestanConfiguration configuration;

    @Override
    @SneakyThrows
    public ECPublicKey getPublicKeyById(String keyId) {
        return new ECPublicKeyImpl(configuration.getPublicKey().getBytes());
    }

    @Override
    @SneakyThrows
    public ECPrivateKey getPrivateKey() {
        return new ECPrivateKeyImpl(configuration.getPrivateKey().getBytes());
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
