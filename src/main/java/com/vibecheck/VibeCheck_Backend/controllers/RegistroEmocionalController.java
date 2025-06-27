package com.vibecheck.VibeCheck_Backend.controllers;

import com.vibecheck.VibeCheck_Backend.dtos.RegistroEmocionalDTO;
import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.services.RegistroEmocionalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO;

import java.util.List;


@RestController
@RequestMapping("/api/registro")
public class RegistroEmocionalController {

    private final RegistroEmocionalService registroService;

    @Autowired
    public RegistroEmocionalController(RegistroEmocionalService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/verificar-codigo")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<Boolean> verificarCodigo(@RequestParam String codigo) {
        boolean valido = registroService.verificarCodigoValido(codigo);
        return ResponseEntity.ok(valido);
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<RegistroEmocionalDTO> registrar(
            @RequestParam String codigo,
            @RequestParam int emocao,
            OAuth2AuthenticationToken authentication) {

        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        RegistroEmocional registro = registroService.registrarEmocao(googleId, codigo, emocao);

        RegistroEmocionalDTO dto = new RegistroEmocionalDTO(
                registro.getId(),
                registro.getEmocao(),
                registro.getCodigoAvaliacaoUsado().getTipo().name(),
                registro.getCodigoAvaliacaoUsado().getDataCriacao().toString(),
                registro.getCodigoAvaliacaoUsado().getProfessor().getNome(),
                registro.getCodigoAvaliacaoUsado().getTurma().getNome()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<DashboardRegistroDTO>> getDashboard() {
        List<DashboardRegistroDTO> registros = registroService.getDashboardRegistros();
        return ResponseEntity.ok(registros);
    }

}
