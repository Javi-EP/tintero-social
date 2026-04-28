package cl.javiep.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    //La clave secreta viene del application.yaml, nunca en el código
    @Value("${jwt.secret}")
    private String secret;

    //Tiempo de expiración en milisegundos (24hrs)
    @Value("${jwt.expiration}")
    private Long expiration;

    // Genera la clave criptográfica a partir del string secreto
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Genera un token JWT para un usuario
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)   // datos extra dentro del token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    // Extrae el email del token
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Extrae el userId del token
    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    // Verifica si el token es válido y no expiró
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;  // token inválido, expirado o mal formado
        }
    }

    // Lee el contenido del token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
