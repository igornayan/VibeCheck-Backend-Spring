package com.vibecheck.VibeCheck_Backend.config;

import com.vibecheck.VibeCheck_Backend.services.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("🔥 Configurando SecurityFilterChain...");

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/", "/login", "/error", "/webjars/**", "/?logout_success=true").permitAll()
                        .requestMatchers("/dashboard", "/check").hasAuthority("ROLE_PROFESSOR")
                        .requestMatchers("/aluno-home").hasAuthority("ROLE_ALUNO")
                        .requestMatchers("/api/codigo/**").hasAuthority("ROLE_PROFESSOR")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(user -> {
                            System.out.println("🔄 Registrando CustomOAuth2UserService no fluxo de autenticação...");
                            user.userService(customOAuth2UserServiceBean()); // Correto!
                        })
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
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserServiceBean() {
        System.out.println("🚀 Criando instância manual de CustomOAuth2UserService...");
        return new CustomOAuth2UserService();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String redirectUrl = "/";

            System.out.println("🎯 Autoridades do usuário após autenticação: " + authorities);

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"))) {
                redirectUrl = "http://localhost:3000/dashboard";
                System.out.println("📌 Redirecionando para: " + redirectUrl);
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"))) {
                redirectUrl = "http://localhost:3000/check";
                System.out.println("📌 Redirecionando para: " + redirectUrl);
            } else {
                System.out.println("❌ Nenhuma role definida! Redirecionando para página padrão.");
            }

            response.sendRedirect(redirectUrl);
        };
    }
}
