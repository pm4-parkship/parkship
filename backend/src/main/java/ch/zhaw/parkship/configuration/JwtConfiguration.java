package ch.zhaw.parkship.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class for configuration of the JWT.
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfiguration {
    private String secret;
    private String algorithm;
    private int expiration;
}
