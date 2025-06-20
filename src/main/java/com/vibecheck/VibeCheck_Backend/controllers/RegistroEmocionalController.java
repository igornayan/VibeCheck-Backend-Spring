package com.vibecheck.VibeCheck_Backend.controllers;

import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.services.RegistroEmocionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registro")
public class RegistroEmocionalController {

    private final RegistroEmocionalService registroService;

    @Autowired
    public RegistroEmocionalController(RegistroEmocionalService registroService) {
        this.registroService = registroService;
    }

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<RegistroEmocional> realizarCheckin(
            @RequestParam String codigo,
            @RequestParam int emocao,
            OAuth2AuthenticationToken authentication) {

        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        RegistroEmocional registro = registroService.realizarCheckin(googleId, codigo, emocao);
        return ResponseEntity.ok(registro);
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<RegistroEmocional> realizarCheckout(
            @RequestParam String codigo,
            @RequestParam int emocao,
            OAuth2AuthenticationToken authentication) {

        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        RegistroEmocional registro = registroService.realizarCheckout(googleId, codigo, emocao);
        return ResponseEntity.ok(registro);
    }
}
