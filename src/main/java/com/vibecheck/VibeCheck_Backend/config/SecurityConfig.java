// Define o pacote onde a classe de configuração está localizada.
package com.vibecheck.VibeCheck_Backend.config;

// Importações necessárias do Spring Security e outros.
import com.vibecheck.VibeCheck_Backend.services.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collection;

/**
 * @Configuration: Marca a classe como uma fonte de definições de beans para o contexto da aplicação.
 * @EnableWebSecurity: Habilita a integração do Spring Security com o Spring MVC. É a anotação principal
 * que ativa a segurança web na sua aplicação.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Este método define o "SecurityFilterChain", que é uma cadeia de filtros que o Spring Security
     * aplica a todas as requisições HTTP que chegam na aplicação. Aqui você define as regras de
     * proteção: quais endpoints são públicos, quais são protegidos e como a autenticação funciona.
     *
     * @param http O objeto principal para configurar a segurança web.
     * @param customOAuth2UserService Um serviço customizado injetado para processar os dados do usuário após o login OAuth2.
     * @return A cadeia de filtros de segurança construída.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                // .cors() aplica a configuração de CORS definida em outro lugar (na sua classe CorsConfig).
                .cors()
                .and()
                // .csrf().disable() desabilita a proteção contra CSRF (Cross-Site Request Forgery).
                // Isso é comum em APIs RESTful que usam tokens (como OAuth2), pois a proteção baseada em tokens
                // já mitiga esse tipo de ataque.
                .csrf().disable()
                // .authorizeHttpRequests(...) inicia a configuração de autorização para as requisições.
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers(...).permitAll() define endpoints que são públicos e não exigem autenticação.
                        // Usado para documentação da API (Swagger/OpenAPI).
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Outros endpoints públicos, como a página inicial, a página de login e recursos estáticos.
                        .requestMatchers("/", "/login", "/error", "/webjars/**", "/?logout_success=true").permitAll()

                        // .requestMatchers(...).hasAuthority(...) define regras de acesso baseadas em "roles" (papéis).
                        // Apenas usuários com a role "ROLE_ALUNO" podem acessar as rotas de registro.
                        .requestMatchers("/api/registro/**").hasAuthority("ROLE_ALUNO")
                        // Apenas usuários com a role "ROLE_PROFESSOR" podem acessar as rotas de código e turmas.
                        .requestMatchers("/api/codigo/**").hasAuthority("ROLE_PROFESSOR")
                        .requestMatchers("/api/turmas/**").hasAuthority("ROLE_PROFESSOR")
                        // .anyRequest().authenticated() é uma regra "catch-all": qualquer outra requisição
                        // que não foi definida acima DEVE ser autenticada. É uma boa prática de segurança.
                        .anyRequest().authenticated()
                )
                // .oauth2Login(...) configura o fluxo de login usando OAuth2 (ex: Login com Google, GitHub, etc.).
                .oauth2Login(oauth -> oauth
                        // .loginPage("/login") especifica que a sua página de login customizada está em "/login".
                        .loginPage("/login")
                        // .userInfoEndpoint(...) customiza como as informações do usuário são obtidas do provedor OAuth2.
                        // Aqui, você delega essa lógica para o seu `CustomOAuth2UserService`.
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        // .successHandler(...) define o que acontece após um login bem-sucedido.
                        // Aqui, ele usa o bean `authenticationSuccessHandler` definido abaixo.
                        .successHandler(authenticationSuccessHandler())
                )
                // .logout(...) configura o processo de logout.
                .logout(logout -> logout
                        // Ação customizada ao fazer logout (aqui, apenas imprime no console).
                        .logoutSuccessHandler((request, response, authentication) -> {
                            System.out.println("✅ Usuário fez logout.");
                        })
                        // Invalida a sessão HTTP do usuário.
                        .invalidateHttpSession(true)
                        // Deleta o cookie de sessão.
                        .deleteCookies("JSESSIONID")
                        // Permite que qualquer um acesse o endpoint de logout.
                        .permitAll()
                );
        // Constrói e retorna o SecurityFilterChain configurado.
        return http.build();
    }

    /**
     * Este bean define um manipulador customizado para o sucesso da autenticação.
     * Sua principal função é redirecionar o usuário para a página correta no frontend
     * com base na sua role (ALUNO ou PROFESSOR).
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Obtém as "authorities" (roles) do usuário autenticado.
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // Define uma URL de redirecionamento padrão.
            String redirectUrl = "/";

            // Verifica se o usuário tem a role de PROFESSOR.
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"))) {
                // Se for professor, redireciona para o dashboard do frontend.
                redirectUrl = "http://localhost:3000/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"))) {
                // Se for aluno, redireciona para a página de check-in do frontend.
                redirectUrl = "http://localhost:3000/check";
            }

            // Envia a resposta de redirecionamento para o navegador do usuário.
            response.sendRedirect(redirectUrl);
        };
    }
}