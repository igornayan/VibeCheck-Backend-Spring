package com.vibecheck.VibeCheck_Backend.controllers;

import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO;
import com.vibecheck.VibeCheck_Backend.dtos.LiberarCodigoRequest;
import com.vibecheck.VibeCheck_Backend.dtos.CodigoAvaliacaoResponseDTO;
import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import com.vibecheck.VibeCheck_Backend.services.CodigoAvaliacaoService;
import com.vibecheck.VibeCheck_Backend.services.RegistroEmocionalService;
import com.vibecheck.VibeCheck_Backend.services.TurmaService;
import com.vibecheck.VibeCheck_Backend.dtos.TurmaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codigo")
public class CodigoAvaliacaoController {

    private final CodigoAvaliacaoService codigoService;
    private final RegistroEmocionalService registroService;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    public CodigoAvaliacaoController(
            CodigoAvaliacaoService codigoService,
            RegistroEmocionalService registroService
    ) {
        this.codigoService = codigoService;
        this.registroService = registroService;
    }

    @PostMapping("/liberar-checkin")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<CodigoAvaliacaoResponseDTO> liberarCheckin(
            @RequestBody LiberarCodigoRequest request,
            OAuth2AuthenticationToken authentication
    ) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        CodigoAvaliacao codigo = codigoService.gerarCodigoCheckin(googleId, request.getNomeTurma());
        return ResponseEntity.ok(new CodigoAvaliacaoResponseDTO(codigo));
    }

    @PostMapping("/liberar-checkout")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<CodigoAvaliacaoResponseDTO> liberarCheckout(
            @RequestBody LiberarCodigoRequest request,
            OAuth2AuthenticationToken authentication
    ) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        CodigoAvaliacao codigo = codigoService.gerarCodigoCheckout(googleId, request.getNomeTurma());
        return ResponseEntity.ok(new CodigoAvaliacaoResponseDTO(codigo));
    }

    @GetMapping("/turmas")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<TurmaDTO>> listarTurmas(OAuth2AuthenticationToken authentication) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        List<TurmaDTO> turmas = turmaService.listarTurmasComIdPorProfessor(googleId);
        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<DashboardRegistroDTO>> getDashboard() {
        List<DashboardRegistroDTO> registros = registroService.getDashboardRegistros();
        return ResponseEntity.ok(registros);
    }
}
