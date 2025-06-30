package com.myapp.Airports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .httpBasic(Customizer.withDefaults())

                .formLogin(form -> form
                        // .loginPage("/login") // Uncomment if you have a custom login page
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .usernameParameter("login")
                        .passwordParameter("password")
                )

                .oauth2Login(oauth -> oauth
                        // .loginPage("/oauth_login") // Uncomment if you have a custom page
                        .failureUrl("/oauth_login?error=true")
                        .defaultSuccessUrl("/admin/dashboard", true)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access_denied")
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**", "/oauth_login/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/airports").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
