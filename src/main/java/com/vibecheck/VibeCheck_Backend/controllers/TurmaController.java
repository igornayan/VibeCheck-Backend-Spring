package com.vibecheck.VibeCheck_Backend.controllers;

import com.vibecheck.VibeCheck_Backend.services.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turmas")
@PreAuthorize("hasRole('PROFESSOR')")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @PutMapping("/{id}")
    public ResponseEntity<String> editarTurma(
            @PathVariable Long id,
            @RequestBody String novoNome
    ) {
        turmaService.atualizarNomeTurma(id, novoNome);
        return ResponseEntity.ok("Nome da turma atualizado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirTurma(@PathVariable Long id) {
        turmaService.excluirTurma(id);
        return ResponseEntity.ok("Turma excluída com sucesso.");
    }
}

