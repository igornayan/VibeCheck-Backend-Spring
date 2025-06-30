// Define o pacote onde a classe Controller está localizada.
package com.vibecheck.VibeCheck_Backend.controllers;

// Importações do Spring Framework e Spring Security.
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Controller: Anotação padrão do Spring para classes que manipulam requisições web.
 * Diferente de @RestController, os métodos aqui podem retornar nomes de views ou,
 * quando anotados com @ResponseBody, retornar dados diretamente no corpo da resposta.
 */
@Controller
public class HomeController {

    /**
     * Este endpoint serve como um gatilho para o login.
     * Ao acessar /login, o usuário é imediatamente redirecionado para a URL padrão
     * do Spring Security que inicia o fluxo de autenticação OAuth2 com o provedor "google".
     * É uma forma elegante de iniciar o login sem precisar de uma página HTML intermediária.
     */
    @GetMapping("/login")
    public RedirectView triggerGoogleLogin() {
        return new RedirectView("/oauth2/authorization/google");
    }

    /**
     * Endpoint para a página inicial (raiz) da aplicação.
     * @ResponseBody garante que a String retornada seja o corpo da resposta HTTP.
     * Este endpoint é público, conforme definido no SecurityConfig.
     */
    @GetMapping("/")
    @ResponseBody
    public String publicPage() {
        return "Bem-vindo ao Vibe Check! Faça login para continuar via /login.";
    }

    /**
     * Endpoint para obter os detalhes do usuário atualmente autenticado.
     * Muito útil para o frontend saber quem é o usuário logado e quais são suas permissões.
     *
     * @param principal A anotação @AuthenticationPrincipal injeta diretamente o objeto
     * do usuário logado (OAuth2User), o que é mais limpo e direto.
     * @return Um ResponseEntity contendo os detalhes do usuário em formato JSON ou um erro 401.
     */
    @GetMapping("/user/details")
    @ResponseBody
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal OAuth2User principal) {
        // Verifica se há um usuário autenticado. Se não, o principal será nulo.
        if (principal == null) {
            // Retorna um erro 401 (Não Autorizado) com uma mensagem JSON.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário não autenticado"));
        }

        // Constrói um mapa com os detalhes do usuário. O Spring converterá este mapa para JSON.
        Map<String, Object> userDetails = Map.of(
                "name", principal.getAttribute("name"),
                "email", principal.getAttribute("email"),
                // Para um OAuth2User, .getName() geralmente retorna o ID único do usuário ('sub' no padrão OIDC).
                "googleId", principal.getName(),
                // Pega as 'authorities' (roles), extrai o nome de cada uma (ex: "ROLE_PROFESSOR")
                // e as coloca em uma lista de Strings.
                "roles", principal.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        // Retorna os detalhes com um status HTTP 200 (OK).
        return ResponseEntity.ok(userDetails);
    }
}