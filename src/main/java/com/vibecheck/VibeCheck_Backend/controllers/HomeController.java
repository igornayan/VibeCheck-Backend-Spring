package com.vibecheck.VibeCheck_Backend.controllers;

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

@Controller
public class HomeController {

    // NOVO MÉTODO PARA LIDAR COM /login
    @GetMapping("/login")
    public RedirectView triggerGoogleLogin() {
        return new RedirectView("/oauth2/authorization/google");
    }

    @GetMapping("/check-auth")
    @ResponseBody
    public ResponseEntity<?> checkAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não está logado.");
        }
        return ResponseEntity.ok("Usuário está logado.");
    }

    @GetMapping("/")
    @ResponseBody
    public String publicPage() {
        return "Bem-vindo ao Vibe Check! Faça login para continuar via /login.";
    }

    @GetMapping("/dashboard")
    @ResponseBody
    public String dashboard() {
        return "Bem-vindo ao painel do professor!";
    }

    @GetMapping("/user/details")
    @ResponseBody
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal OAuth2User principal) {
        // A anotação @AuthenticationPrincipal OAuth2User já injeta o usuário logado.
        if (principal == null) {
            // Retorna um erro 401 (Não Autorizado) com uma mensagem JSON
            return ResponseEntity.status(401).body(Map.of("error", "Usuário não autenticado"));
        }

        // Monta um Map. O Spring irá convertê-lo para um objeto JSON automaticamente.
        Map<String, Object> userDetails = Map.of(
                "name", principal.getAttribute("name"),
                "email", principal.getAttribute("email"),
                "googleId", principal.getName(), // O nome principal do OAuth2User é o 'sub' (ID do Google)
                "roles", principal.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        // Retorna 200 OK com o objeto JSON contendo os detalhes do usuário
        return ResponseEntity.ok(userDetails);
    }
}