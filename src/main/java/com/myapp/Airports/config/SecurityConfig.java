package com.myapp.Airports.config;

import com.myapp.Airports.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )

                .formLogin(form -> form
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .usernameParameter("login")
                        .passwordParameter("password")
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access_denied")
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login/**",
                                "/user/login"
                        ).permitAll()

                        .requestMatchers(
                                "/api/user/login"
                        ).permitAll()

                        .requestMatchers(
                                "/user/cabinet",
                                "/user/logout",
                                "/api/user/**"
                        ).authenticated()

                        .requestMatchers("/admin/**")
                        .hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/airports"
                        )
                        .hasAuthority("ADMIN")

                        .anyRequest()
                        .permitAll()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
