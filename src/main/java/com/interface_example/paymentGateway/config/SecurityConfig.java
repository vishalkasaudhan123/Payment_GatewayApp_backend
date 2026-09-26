//	package com.interface_example.paymentGateway.config;
//	
//	import org.springframework.context.annotation.Bean;
//	import org.springframework.context.annotation.Configuration;
//	import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//	import org.springframework.security.web.SecurityFilterChain;
//	import org.springframework.web.cors.CorsConfiguration;
//	import org.springframework.web.cors.CorsConfigurationSource;
//	import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//	
//	import java.util.List;
//	
//	/**
//	 * Configures Spring Security and CORS for the application.
//	 * Currently permits all requests (dev-mode setup, not production-ready).
//	 */
//	@Configuration
//	public class SecurityConfig {
//	
//	
//		/**
//	     * Defines the security filter chain: disables CSRF, enables CORS,
//	     * and permits all HTTP requests without authentication.
//	     */
//	    @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http)
//	            throws Exception {
//	
//	
//	        http
//	            .csrf(csrf -> csrf.disable())
//	
//	            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//	
//	            .authorizeHttpRequests(auth -> auth
//	                    .requestMatchers("/pay").permitAll()
//	                    .anyRequest().permitAll()
//	            );
//	
//	
//	        return http.build();
//	
//	    }
//	
//	
//	
//	    /**
//	     * Allows requests from the React frontend (localhost:3000)
//	     * with common HTTP methods and any header.
//	     */
//	    @Bean
//	    public CorsConfigurationSource corsConfigurationSource(){
//	
//	        CorsConfiguration configuration =
//	                new CorsConfiguration();
//	
//	
//	        configuration.setAllowedOrigins(
//	                List.of("http://localhost:3000")
//	        );
//	
//	
//	        configuration.setAllowedMethods(
//	                List.of(
//	                        "GET",
//	                        "POST",
//	                        "PUT",
//	                        "DELETE",
//	                        "OPTIONS"
//	                )
//	        );
//	
//	
//	        configuration.setAllowedHeaders(
//	                List.of("*")
//	        );
//	
//	
//	        UrlBasedCorsConfigurationSource source =
//	                new UrlBasedCorsConfigurationSource();
//	
//	
//	        source.registerCorsConfiguration(
//	                "/**",
//	                configuration
//	        );
//	
//	
//	        return source;
//	
//	    }
//	
//	}




package com.interface_example.paymentGateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures Spring Security and CORS for the application.
 *
 * Currently permits all requests.
 * This configuration is suitable for development.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines the security filter chain.
     *
     * CSRF is disabled for the REST API.
     * CORS is enabled.
     * All requests are permitted.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }

    /**
     * Configures CORS for the frontend application.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:3000"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}