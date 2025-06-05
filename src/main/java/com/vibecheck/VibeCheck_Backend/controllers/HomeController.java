package com.vibecheck.VibeCheck_Backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HomeController {

    // NOVO MÉTODO PARA LIDAR COM /login
    @GetMapping("/login")
    public String triggerGoogleLogin() {
        // Esta URL é o endpoint padrão do Spring Security para iniciar o fluxo OAuth2
        // com o provedor registrado como "google" (definido em application.properties)
        return "redirect:/oauth2/authorization/google";
    }

    // Endpoint para onde o usuário é redirecionado após o login (definido no SecurityConfig)
    // Nota: Seu AuthenticationSuccessHandler agora redireciona para o frontend (localhost:3000)
    // então este endpoint /home no backend pode não ser diretamente acessado pelo usuário após o login,
    // mas pode ser útil para testes internos ou se o frontend chamar uma API /home.
    @GetMapping("/home")
    public String homePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getAttribute("name"));
            model.addAttribute("userEmail", principal.getAttribute("email"));
        }
        // Se você tiver um template home.html, ele será renderizado.
        // Caso contrário, este método precisa ser @ResponseBody ou retornar um redirect.
        // Por agora, vamos assumir que ele não é o destino final do usuário.
        return "home_placeholder"; // Crie um home_placeholder.html ou ajuste
    }

    @GetMapping("/")
    @ResponseBody
    public String publicPage() {
        return "Bem-vindo ao Vibe Check! Faça login para continuar via /login.";
    }

    @GetMapping("/user/details")
    @ResponseBody
    public ResponseEntity<?> getUserDetails(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            // Isso pode acontecer se o usuário não estiver logado via OAuth2
            // ou se a sessão expirou e a requisição não foi autenticada.
            if (authentication != null && authentication.isAuthenticated()) {
                // Poderia ser um tipo diferente de autenticação, mas improvável no nosso setup atual.
                return ResponseEntity.ok("Usuário autenticado (não OAuth2): " + authentication.getName());
            }
            return ResponseEntity.status(401).body("Nenhum usuário OAuth2 autenticado. Por favor, faça login via /login.");
        }

        Map<String, Object> attributes = oauth2User.getAttributes();
        String nameAttributeKey = oauth2User.getName();

        StringBuilder details = new StringBuilder();
        details.append("<h2>Detalhes do Usuário OAuth2:</h2>");
        details.append("Nome Principal (").append(nameAttributeKey).append("): ").append(oauth2User.getName()).append("<br>");
        details.append("Authorities (Roles): ").append(oauth2User.getAuthorities()).append("<br>");
        details.append("Atributos Completos: <pre>").append(attributes.toString()).append("</pre><br>");

        return ResponseEntity.ok(details.toString());
    }
}