package cl.javiep.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {

    // BCrypt es el algoritmo estándar para hashear contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // deshabilitado para APIs REST
                .authorizeHttpRequests(auth -> auth
                        // estos endpoints son públicos — no requieren token
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/validate",  // para que otros microservicios validen tokens
                                "/h2-console/**"
                        ).permitAll()
                        // cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())  // necesario para h2-console
                );
        return http.build();
    }
}