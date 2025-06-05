package com.vibecheck.VibeCheck_Backend.config;

import com.vibecheck.VibeCheck_Backend.services.CustomOAuth2UserService; // Certifique-se que este import está correto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error", "/webjars/**", "/?logout_success=true").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(authenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout_success=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String redirectUrl = "/";

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"))) {
                redirectUrl = "http://localhost:3000/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"))) {
                redirectUrl = "http://localhost:3000/check";
            }

            response.sendRedirect(redirectUrl);
        };
    }
}
