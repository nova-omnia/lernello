package ch.nova_omnia.lernello.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration for the web security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${cors.allowed-origins}")
    private String corsAllowedOrigins;

    public static final String[] WHITELIST_URLS = {"/api/auth/**", "/error", "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**", "/h2-console/**"
    };

    @Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    AuthenticationManager authenticationManager(
                                                AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = Arrays.asList(corsAllowedOrigins.split(","));
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Initializes and configures the application's security filter chain.
     * <p>
     * This method is invoked once during application startup to set up the HTTP
     * security configuration. This allows for configuring the security filter chain
     * that will be applied to all HTTP requests.
     * There is no need to check each request individually, as the filter chain
     * will be applied to all requests.
     * </p>
     *
     * @param http the {@link HttpSecurity} instance used to configure web security
     * @return the fully configured {@link SecurityFilterChain} applied to all HTTP requests
     * @throws Exception if an error occurs during the configuration process
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Allow H2 console from the same origin  
                .headers((headers) -> headers.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.sameOrigin()
                ))
                // Disable session management
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure the authorization of requests
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(WHITELIST_URLS).permitAll() // Allow all requests to /api/auth/**
                                .anyRequest().authenticated() // All other requests require authentication
                );
        // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
