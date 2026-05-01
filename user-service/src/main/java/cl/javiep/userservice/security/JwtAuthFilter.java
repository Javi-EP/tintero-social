package cl.javiep.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// OncePerRequestFilter garantiza que este filtro se ejecute
// exactamente una vez por cada petición HTTP
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leer el header Authorization de la petición
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer ", dejar pasar
        //    Spring Security decidirá si el endpoint requiere auth o no
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token — quitar el prefijo "Bearer "
        String token = authHeader.substring(7);

        // 4. Validar el token
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.getEmailFromToken(token);

            // 5. Crear el objeto de autenticación que Spring Security entiende
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,   // principal (quién es)
                            null,    // credentials (no necesario con JWT)
                            List.of() // authorities (roles — vacío por ahora)
                    );

            // 6. Registrar la autenticación en el contexto de Spring Security
            //    A partir de acá Spring sabe que esta petición está autenticada
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}