package com.proyecto.fenixtech.config; 

import com.proyecto.fenixtech.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Inyectamos nuestro servicio JWT y el servicio de usuarios de Spring
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
             HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener la cabecera 'Authorization'
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si no hay cabecera o no empieza por "Bearer ", le dejamos pasar al siguiente filtro 
        // (Spring Security lo bloqueará más adelante si la ruta era privada)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitamos los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);
        
        // 4. Extraer el email del token usando nuestra máquina
        userEmail = jwtService.extractUsername(jwt);

        // 5. Si hay email y el usuario no está ya autenticado en este contexto
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Buscamos al usuario en la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Comprobamos si el token es válido y no ha caducado
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // Creamos el "pase VIP" oficial de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                // Le añadimos detalles de la petición (IP, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Permitimos el acceso
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 6. Pase lo que pase, continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}