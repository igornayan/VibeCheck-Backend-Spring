package com.vibecheck.VibeCheck_Backend.controllers;

import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import com.vibecheck.VibeCheck_Backend.services.CodigoAvaliacaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/codigo")
public class CodigoAvaliacaoController {

    private final CodigoAvaliacaoService codigoService;

    @Autowired
    public CodigoAvaliacaoController(CodigoAvaliacaoService codigoService) {
        this.codigoService = codigoService;
    }

    @PostMapping("/liberar-checkin")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<CodigoAvaliacao> liberarCheckin(OAuth2AuthenticationToken authentication) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        CodigoAvaliacao codigo = codigoService.gerarCodigoCheckin(googleId);
        return ResponseEntity.ok(codigo);
    }

    @PostMapping("/liberar-checkout")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<CodigoAvaliacao> liberarCheckout(OAuth2AuthenticationToken authentication) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        CodigoAvaliacao codigo = codigoService.gerarCodigoCheckout(googleId);
        return ResponseEntity.ok(codigo);
    }
}
