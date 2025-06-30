// Define o pacote onde a classe Controller está localizada.
package com.vibecheck.VibeCheck_Backend.controllers;

// Importações de DTOs (Data Transfer Objects), Models e Services.
import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO;
import com.vibecheck.VibeCheck_Backend.dtos.LiberarCodigoRequest;
import com.vibecheck.VibeCheck_Backend.dtos.CodigoAvaliacaoResponseDTO;
import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import com.vibecheck.VibeCheck_Backend.services.CodigoAvaliacaoService;
import com.vibecheck.VibeCheck_Backend.services.RegistroEmocionalService;
import com.vibecheck.VibeCheck_Backend.services.TurmaService;
import com.vibecheck.VibeCheck_Backend.dtos.TurmaDTO;

// Importações do Spring Framework.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController: Combina @Controller e @ResponseBody. Indica que a classe é um controlador de API
 * e que os retornos dos métodos serão automaticamente serializados para JSON e enviados no corpo da resposta.
 * @RequestMapping("/api/codigo"): Define que todos os endpoints neste controller começarão com o prefixo "/api/codigo".
 */
@RestController
@RequestMapping("/api/codigo")
public class CodigoAvaliacaoController {

    // Injeção das camadas de serviço (Services) que contêm a lógica de negócio.
    private final CodigoAvaliacaoService codigoService;
    private final RegistroEmocionalService registroService;

    // A injeção de dependência via @Autowired em campos é funcional, mas a injeção via construtor (abaixo)
    // é geralmente preferida por facilitar os testes e garantir que as dependências obrigatórias existam.
    @Autowired
    private TurmaService turmaService;

    /**
     * Construtor para injeção de dependências. Esta é a forma recomendada pelo Spring para injetar
     * dependências obrigatórias.
     */
    @Autowired
    public CodigoAvaliacaoController(
            CodigoAvaliacaoService codigoService,
            RegistroEmocionalService registroService
    ) {
        this.codigoService = codigoService;
        this.registroService = registroService;
    }

    /**
     * Endpoint para um professor gerar um código de CHECK-IN para uma turma.
     * @PostMapping: Mapeia para requisições HTTP POST em "/api/codigo/liberar-checkin".
     * @PreAuthorize: Garante que apenas usuários com a role 'PROFESSOR' podem acessar este método.
     * A segurança é verificada ANTES da execução do método.
     */
    @PostMapping("/liberar-checkin")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<CodigoAvaliacaoResponseDTO> liberarCheckin(
            @RequestBody LiberarCodigoRequest request, // Pega o corpo da requisição JSON e transforma no DTO.
            OAuth2AuthenticationToken authentication // Injetado pelo Spring Security, contém os dados do usuário logado.
    ) {
        // Extrai o ID do Google ('sub') do token de autenticação para identificar o professor de forma segura.
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        // Delega a lógica de negócio para o Service.
        CodigoAvaliacao codigo = codigoService.gerarCodigoCheckin(googleId, request.getNomeTurma());
        // Retorna o código gerado com um status HTTP 200 (OK).
        return ResponseEntity.ok(new CodigoAvaliacaoResponseDTO(codigo));
    }

    /**
     * Endpoint para um professor gerar um código de CHECK-OUT.
     * A lógica é idêntica à do check-in, mas chama um método de serviço diferente.
     */
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

    /**
     * Endpoint para listar todas as turmas associadas ao professor logado.
     * @GetMapping: Mapeia para requisições HTTP GET em "/api/codigo/turmas".
     */
    @GetMapping("/turmas")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<TurmaDTO>> listarTurmas(OAuth2AuthenticationToken authentication) {
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");
        List<TurmaDTO> turmas = turmaService.listarTurmasComIdPorProfessor(googleId);
        return ResponseEntity.ok(turmas);
    }

    /**
     * Endpoint para obter os dados do dashboard geral.
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<DashboardRegistroDTO>> getDashboard() {
        // Este método busca registros gerais do dashboard. Não parece filtrar por professor.
        List<DashboardRegistroDTO> registros = registroService.getDashboardRegistros();
        return ResponseEntity.ok(registros);
    }
}