package za.co.patrick.finance.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
class SecurityConfiguration {

    @Bean
    SecurityFilterChain applicationSecurity(HttpSecurity http, SecurityProperties securityProperties) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable());

        if (securityProperties.enabled()) {
            http.authorizeHttpRequests(registry -> registry
                            .requestMatchers(
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/actuator/health",
                                    "/actuator/info"
                            ).permitAll()
                            .anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults());
        } else {
            http.authorizeHttpRequests(registry -> registry.anyRequest().permitAll())
                    .httpBasic(httpBasic -> httpBasic.disable());
        }
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(SecurityProperties securityProperties, PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(User.builder()
                .username(securityProperties.username())
                .password(passwordEncoder.encode(securityProperties.password()))
                .roles(securityProperties.role())
                .build());
    }
}
