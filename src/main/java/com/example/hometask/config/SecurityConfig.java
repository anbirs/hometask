package com.example.hometask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/users/register").permitAll()

                        .requestMatchers(HttpMethod.GET, "/v1/movies/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/movies/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/v1/showtimes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/showtimes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v1/showtimes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/showtimes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/v1/tickets/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/v1/tickets/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/tickets/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/v1/tickets/**").hasRole("CUSTOMER")


                        .requestMatchers("/v1/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
