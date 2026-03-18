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
                                                .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**",
                                                                "/swagger-ui.html", "/uploads/**")
                                                .permitAll()

                                                // Acceso Público
                                                .requestMatchers(HttpMethod.GET, "/users/me").authenticated()
                                                
                                                .requestMatchers(HttpMethod.GET, "/companies", "/companies/{id}",
                                                                "/companies/top", "/count",
                                                                "/products/**", "/productsImg/**", "/postsImg/**",
                                                                "/comments/posts/{postId}", "/categories",
                                                                "/categories/{id}", "/subcategories",
                                                                "/subcategories/{id}", "/subcategories/category/{id}",
                                                                "/company-badges/company/{companyId}/count",
                                                                "/company-badges/{companyId}/{badgeId}",
                                                                "/shipping-carriers", "/shipping-carriers/{id}",
                                                                "/shipping-carriers/{id}/track/{number}",
                                                                "/posts/user/{userId}", "/posts/{id}",
                                                                "/reviews/company/**")
                                                .permitAll()

                                                // Acceso solo admin
                                                .requestMatchers(HttpMethod.GET, "/addresses", "/addresses/{id}",
                                                                "/addresses/filters",
                                                                "/addresses/count", "/badges/**", "/cart_items",
                                                                "/cart_items/{id}",
                                                                "/cart_items/product/{id}", "/cart_items/quantity",
                                                                "/cart_items/count",
                                                                "/companies/all", "/companies/search/impact",
                                                                "/comments/users/{userId}", "/comments/count",
                                                                "/proposals/count", "/proposals/user/{id}",
                                                                "/proposals/all", "/order-details/**",
                                                                "/categories/count", "/categories/name",
                                                                "/subcategories/count", "/subcategories/search",
                                                                "/orders", "/orders/count", "/orders/filters",
                                                                "/shipments", "/shipments/filters", "/shipments/count",
                                                                "/company-badges/count", "/company-badges",
                                                                "/company-badges/company/{id}",
                                                                "/company-badges/awarded_at",
                                                                "/shipping-carriers/admin/**", "/users/{id}",
                                                                "/users/search", "/users/email/**", "/posts/count",
                                                                "/posts", "/reviews", "/reviews/count",
                                                                "/reviews/user/{userId}", "/contact/**")
                                                .hasRole("ADMIN")

                                                // Acceso privado particular
                                                .requestMatchers(HttpMethod.GET, "/addresses/user/{id}",
                                                                "/cart_items/user/{id}", "/cart_items/my/count",
                                                                "/follows/user/{userId}/**", "/orders/buyer/{id}",
                                                                "/orders/{id}", "/shipments/{id}",
                                                                "/shipments/order/{id}")
                                                .hasAnyRole("PARTICULAR", "ADMIN")

                                                // Acceso privado empresas
                                                .requestMatchers(HttpMethod.GET, "/companies/user/{userId}",
                                                                "/proposals", "/proposals/{id}",
                                                                "/follows/company/{companyId}/**")
                                                .hasAnyRole("EMPRESA", "ADMIN")

                                                //Métodos de inserción 
                                                .requestMatchers(HttpMethod.POST, "/badges", "/categories",
                                                                "/subcategories", "/company-badges",
                                                                "/shipping-carriers")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/addresses").hasAnyRole("PARTICULAR", "EMPRESA")
                                                .requestMatchers(HttpMethod.POST, "/addresses", "/comments",
                                                                "/cart_items", "/proposals", "/follows/toggle",
                                                                "/orders/checkout", "/shipments", "/reviews", "/contact")
                                                .hasRole("PARTICULAR")
                                                .requestMatchers(HttpMethod.POST, "/products", "/users/me/password",
                                                                "/posts")
                                                .hasRole("EMPRESA")

                                                //Métodos de actualización
                                                
                                                .requestMatchers(HttpMethod.PUT, "/users/me", "/addresses/{id}").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/cart_items/{id}",
                                                                "/reviews/{id}")
                                                .hasAnyRole("PARTICULAR", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/badges/{id}", "/categories",
                                                                "/subcategories", "/orders/{id}/status",
                                                                "/shipping-carriers/{id}")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/companies/{id}", "/products/{id}",
                                                                "/proposals/{id}", "/posts/{id}")
                                                .hasAnyRole("EMPRESA", "ADMIN")
                                                
                                                
                                                //Métodos de eliminación
                                                .requestMatchers(HttpMethod.DELETE, "/addresses/{id}").authenticated()
                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/cart_items/{id}", "/comments/{id}/user/{userId}",
                                                                "/reviews/{id}")
                                                .hasAnyRole("PARTICULAR", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/companies/{id}", "/products/{id}",
                                                                "/productsImg/{id}",
                                                                "/postsImg/post/{postId}/image/{imageId}",
                                                                "/posts/{id}")
                                                .hasAnyRole("EMPRESA", "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/badges/{id}", "/proposals/{id}",
                                                                "/categories", "/subcategories",
                                                                "/company/{companyId}/badge/{badgeId}",
                                                                "/shipping-carriers/{id}")
                                                .hasRole("ADMIN")

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