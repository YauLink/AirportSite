package com.myapp.Airports.config;

import com.myapp.Airports.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.header.writers.StaticHeadersWriter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/login", "/api/user/login"))

                .headers(headers -> {
                    headers.contentTypeOptions(Customizer.withDefaults());
                    headers.frameOptions(frame -> frame.deny());
                    headers.addHeaderWriter(new StaticHeadersWriter(
                            "Strict-Transport-Security",
                            "max-age=31536000; includeSubDomains"));
                })

                .formLogin(form -> form
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .usernameParameter("login")
                        .passwordParameter("password"))

                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true"))

                .sessionManagement(session -> session
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession()))

                .exceptionHandling(ex -> ex.accessDeniedPage("/access_denied"))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/access_denied", "/css/**",
                                "/login/**", "/user/login", "/api/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/airports", "/flights",
                                "/filters", "/api/flights/**", "/api/filters").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**")
                        .hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/user/**", "/user/**")
                        .hasAuthority("USER")
                        .requestMatchers("/api/admin/**", "/admin/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers("/api/bookings/**", "/bookings/**", "/api/tickets/**", "/tickets/**",
                                "/flights/new/**", "/flights/edit/**", "/flights/delete/**")
                        .hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/airports").hasAuthority("ADMIN")
                        .anyRequest().authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
