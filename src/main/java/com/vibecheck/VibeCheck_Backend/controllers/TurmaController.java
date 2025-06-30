package com.vibecheck.VibeCheck_Backend.controllers;

import com.vibecheck.VibeCheck_Backend.services.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @RestController: Define a classe como um controlador de API REST.
 * @RequestMapping("/api/turmas"): Mapeia todos os endpoints para o prefixo "/api/turmas".
 * @PreAuthorize("hasRole('PROFESSOR')"): IMPORTANTE! Esta anotação de segurança está a nível de classe.
 * Isso significa que TODOS os métodos dentro deste controller exigirão que o usuário
 * tenha a role 'PROFESSOR'. É uma forma limpa de proteger um recurso inteiro.
 */
@RestController
@RequestMapping("/api/turmas")
@PreAuthorize("hasRole('PROFESSOR')")
public class TurmaController {

    // Injeção de dependência via campo. Funciona, mas a injeção via construtor é
    // geralmente preferida por questões de testabilidade e clareza.
    @Autowired
    private TurmaService turmaService;

    /**
     * Endpoint para editar o nome de uma turma existente.
     * @PutMapping: Mapeia para requisições HTTP PUT, método apropriado para atualização de um recurso.
     * @param id O ID da turma a ser editada, extraído da URL (ex: /api/turmas/123).
     * @param novoNome O novo nome para a turma, extraído do corpo (body) da requisição.
     * Atenção: Receber um JSON simples como uma String pode ser problemático.
     * É mais robusto usar um DTO. (Veja a análise abaixo).
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> editarTurma(
            @PathVariable Long id,
            @RequestBody String novoNome
    ) {
        turmaService.atualizarNomeTurma(id, novoNome);
        return ResponseEntity.ok("Nome da turma atualizado com sucesso.");
    }

    /**
     * Endpoint para excluir uma turma.
     * @DeleteMapping: Mapeia para requisições HTTP DELETE, método correto para exclusão.
     * @param id O ID da turma a ser excluída, extraído da URL.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirTurma(@PathVariable Long id) {
        turmaService.excluirTurma(id);
        return ResponseEntity.ok("Turma excluída com sucesso.");
    }
}