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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/login", "/api/user/login"))

                .formLogin(form -> form
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .usernameParameter("login")
                        .passwordParameter("password"))

                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true"))

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
