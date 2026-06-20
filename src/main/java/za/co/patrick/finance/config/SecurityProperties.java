package za.co.patrick.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
        boolean enabled,
        String username,
        String password,
        String role
) {
}
