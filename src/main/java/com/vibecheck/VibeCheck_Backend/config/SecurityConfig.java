package com.vibecheck.VibeCheck_Backend.config;

import com.vibecheck.VibeCheck_Backend.services.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/", "/login", "/error", "/webjars/**", "/?logout_success=true").permitAll()
                        .requestMatchers("/dashboard", "/check").hasAuthority("ROLE_PROFESSOR")
                        .requestMatchers("/aluno-home").hasAuthority("ROLE_ALUNO")
                        .requestMatchers("/api/registro/**").hasAuthority("ROLE_ALUNO")
                        .requestMatchers("/api/codigo/**").hasAuthority("ROLE_PROFESSOR")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .successHandler(authenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            System.out.println("✅ Usuário fez logout.");
                            response.sendRedirect("http://localhost:3000/login");
                        })
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
