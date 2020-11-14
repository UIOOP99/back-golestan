package ir.ui.golestan;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "golestan")
public class GolestanConfiguration {

    private String publicKey;

    private String privateKey;

    private String authHeader;
}
