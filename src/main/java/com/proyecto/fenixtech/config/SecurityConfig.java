package com.proyecto.fenixtech.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.proyecto.fenixtech.config.JwtAuthenticationFilter;

import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Spring buscará estos Beans automáticamente porque ya los creamos antes
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivamos protección web clásica
                .authorizeHttpRequests(auth -> auth
                        // 1. Puertas abiertas (¡Faltaba el .permitAll() aquí!)
                        .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()

                        // Acceso Público
                        .requestMatchers(HttpMethod.GET, "/companies", "/companies/{id}", "/companies/top", "/count",
                                "/products/**", "/productsImg/**", "/postsImg/**")
                        .permitAll()

                        // Acceso solo admin
                        .requestMatchers(HttpMethod.GET, "/addresses", "/addresses/{id}", "/addresses/filters",
                                "/addresses/count", "/badges/**", "/cart_items", "/cart_items/{id}",
                                "/cart_items/product/{id}", "/cart_items/quantity", "/cart_items/count",
                                "companies/all", "/companies/search/impact")
                        .hasRole("ADMIN")

                        // Acceso privado
                        .requestMatchers(HttpMethod.GET, "/addresses/user/{id}", "/cart_items/user/{id}",
                                "/cart_items/my/count")
                        .hasAnyRole("PARTICULAR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/companies/user/{userId}").hasAnyRole("EMPRESA", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/addresses", "/cart_items").hasRole("PARTICULAR")
                        .requestMatchers(HttpMethod.POST, "/badges").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("EMPRESA")

                        .requestMatchers(HttpMethod.PUT, "/addresses/{id}", "/cart_items/{id}").hasRole("PARTICULAR")
                        .requestMatchers(HttpMethod.PUT, "/badges/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/companies/{id}", "/products/{id}")
                        .hasAnyRole("EMPRESA", "ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/addresses/{id}", "/cart_items/{id}")
                        .hasAnyRole("PARTICULAR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/companies/{id}", "/products/{id}", "/productsImg/{id}",
                                "/postsImg/post/{postId}/image/{imageId}")
                        .hasAnyRole("EMPRESA", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/badges/{id}").hasRole("ADMIN")

                        // 4. El resto requiere estar logueado
                        .anyRequest().authenticated())
                // No guardamos sesiones en memoria
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Mapeamos las credenciales en ApplicationConfig
                .authenticationProvider(authenticationProvider)
                // Ponemos a JWT como filtro
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}