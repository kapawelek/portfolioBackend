package org.springframework.samples.petclinic.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@ConditionalOnProperty(
    name = "app.security.enabled",
    havingValue = "true",
    matchIfMissing = true // jeśli nie ustawisz property, security będzie WŁĄCZONE
)
public class SecurityConfig {

    @Bean(name = "securityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(reg -> reg
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/actuator/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {
            });
        return http.build();
    }
}
