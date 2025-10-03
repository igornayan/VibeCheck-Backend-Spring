// Define o pacote para as classes de controlador.
package com.vibecheck.VibeCheck_Backend.controllers;

// Importações necessárias.
import com.vibecheck.VibeCheck_Backend.dtos.PraticaDTO;
import com.vibecheck.VibeCheck_Backend.dtos.PraticaResumoDTO;
import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.AlunoRepository;
import com.vibecheck.VibeCheck_Backend.repositories.TurmaRepository;
import com.vibecheck.VibeCheck_Backend.services.PraticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @RestController: Define esta classe como um controlador de API REST.
 * @RequestMapping("/api/praticas"): Todos os endpoints aqui serão prefixados com "/api/praticas".
 */
@RestController
@RequestMapping("/api/praticas")
public class PraticaController {

    // Injeção de dependências
    private final PraticaService praticaService;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    /**
     * Injeção de dependência via construtor (prática recomendada).
     * 
     * @param praticaService Serviço para operações com práticas.
     * @param alunoRepository Repositório para operações com alunos.
     * @param turmaRepository Repositório para operações com turmas.
     */
    @Autowired
    public PraticaController(PraticaService praticaService, 
                           AlunoRepository alunoRepository, 
                           TurmaRepository turmaRepository) {
        this.praticaService = praticaService;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    /**
     * Endpoint para listar todas as práticas (apenas para professores).
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas usuários com a role 'PROFESSOR' possam acessar.
     * @return Lista de práticas resumidas.
     */
    @GetMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<PraticaResumoDTO>> listarTodas() {
        List<PraticaResumoDTO> dtos = praticaService.listarTodasPraticas();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para buscar uma prática específica por ID.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que usuários autenticados possam acessar.
     * @param id ID da prática.
     * @return Dados completos da prática.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ALUNO', 'PROFESSOR')")
    public ResponseEntity<PraticaDTO> buscarPorId(@PathVariable Long id) {
        Optional<Pratica> pratica = praticaService.buscarPorId(id);
        if (pratica.isPresent()) {
            return ResponseEntity.ok(new PraticaDTO(pratica.get()));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para listar práticas de uma turma específica.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas professores possam acessar.
     * @param turmaId ID da turma.
     * @return Lista de práticas da turma.
     */
    @GetMapping("/turma/{turmaId}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<PraticaResumoDTO>> listarPorTurma(@PathVariable Long turmaId) {
        turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
        
        List<PraticaResumoDTO> dtos = praticaService.listarPraticasPorTurma(turmaId);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para listar práticas abertas de uma turma.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas professores possam acessar.
     * @param turmaId ID da turma.
     * @return Lista de práticas abertas da turma.
     */
    @GetMapping("/turma/{turmaId}/abertas")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<PraticaResumoDTO>> listarAbertasPorTurma(@PathVariable Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
        
        List<PraticaResumoDTO> dtos = praticaService.listarPraticasAbertasPorTurma(turmaId, turma);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para listar práticas abertas do aluno autenticado.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas alunos possam acessar.
     * @param authentication Token de autenticação para identificar o aluno.
     * @return Lista de práticas abertas do aluno.
     */
    @GetMapping("/minhas/abertas")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<List<PraticaResumoDTO>> listarMinhasAbertas(OAuth2AuthenticationToken authentication) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        Aluno aluno = alunoRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));
        
        List<PraticaResumoDTO> dtos = praticaService.listarMinhasPraticasAbertas(aluno, authentication);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para listar práticas de um aluno em um período específico.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas professores possam acessar.
     * @param alunoId ID do aluno.
     * @param inicio Data de início do período (formato: yyyy-MM-ddTHH:mm:ss).
     * @param fim Data de fim do período (formato: yyyy-MM-ddTHH:mm:ss).
     * @return Lista de práticas do aluno no período.
     */
    @GetMapping("/aluno/{alunoId}/periodo")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<PraticaResumoDTO>> listarPorAlunoEPeriodo(
            @PathVariable Long alunoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));
        
        List<Pratica> praticas = praticaService.buscarPraticasPorAlunoEPeriodo(aluno, inicio, fim);
        List<PraticaResumoDTO> dtos = praticas.stream()
                .map(PraticaResumoDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para listar práticas de uma turma em um período específico.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas professores possam acessar.
     * @param turmaId ID da turma.
     * @param inicio Data de início do período (formato: yyyy-MM-ddTHH:mm:ss).
     * @param fim Data de fim do período (formato: yyyy-MM-ddTHH:mm:ss).
     * @return Lista de práticas da turma no período.
     */
    @GetMapping("/turma/{turmaId}/periodo")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<PraticaResumoDTO>> listarPorTurmaEPeriodo(
            @PathVariable Long turmaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
        
        List<PraticaResumoDTO> dtos = praticaService.listarPraticasPorTurmaEPeriodo(turmaId, turma, inicio, fim);
        return ResponseEntity.ok(dtos);
    }
}
