package cl.javiep.userservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "clave-super-segura-que-mide-al-menos-32-caracteres-para-hmac");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Test
    void generateAndValidateToken_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtUtil.generateToken(1L, "user@example.com");

        boolean valid = jwtUtil.validateToken(token);

        assertThat(valid).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalse_whenTokenIsInvalid() {
        boolean valid = jwtUtil.validateToken("token.invalido.xyz");

        assertThat(valid).isFalse();
    }

    @Test
    void getEmailFromToken_shouldReturnEmail_whenTokenIsValid() {
        String token = jwtUtil.generateToken(1L, "user@example.com");

        String email = jwtUtil.getEmailFromToken(token);

        assertThat(email).isEqualTo("user@example.com");
    }

    @Test
    void getUserIdFromToken_shouldReturnUserId_whenTokenIsValid() {
        String token = jwtUtil.generateToken(42L, "user@example.com");

        Long userId = jwtUtil.getUserIdFromToken(token);

        assertThat(userId).isEqualTo(42L);
    }
}
