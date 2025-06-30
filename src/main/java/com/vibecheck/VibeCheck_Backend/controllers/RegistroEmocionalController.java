package com.vibecheck.VibeCheck_Backend.controllers;

// Importações de DTOs, Models e Services.
import com.vibecheck.VibeCheck_Backend.dtos.RegistroEmocionalDTO;
import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.services.RegistroEmocionalService;

// Importações do Spring Framework e Spring Security.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO; // Esta importação não está sendo usada no arquivo.
import java.util.List; // Esta importação não está sendo usada no arquivo.

/**
 * @RestController: Define esta classe como um controlador de API REST.
 * @RequestMapping("/api/registro"): Todos os endpoints aqui serão prefixados com "/api/registro".
 */
@RestController
@RequestMapping("/api/registro")
public class RegistroEmocionalController {

    // A camada de serviço que contém a lógica de negócio para os registros.
    private final RegistroEmocionalService registroService;

    /**
     * Injeção de dependência via construtor (prática recomendada).
     * Garante que o controller tenha uma instância do serviço para funcionar.
     */
    @Autowired
    public RegistroEmocionalController(RegistroEmocionalService registroService) {
        this.registroService = registroService;
    }

    /**
     * Endpoint para um aluno verificar se um código (de check-in ou check-out) é válido.
     * @GetMapping: Mapeia para requisições HTTP GET.
     * @PreAuthorize: Garante que apenas usuários com a role 'ALUNO' possam acessar.
     * @param codigo O código a ser verificado, recebido como um parâmetro na URL
     * (ex: /api/registro/verificar-codigo?codigo=XYZ123).
     * @return ResponseEntity contendo 'true' se o código for válido, ou 'false' caso contrário.
     */
    @GetMapping("/verificar-codigo")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<Boolean> verificarCodigo(@RequestParam String codigo) {
        boolean valido = registroService.verificarCodigoValido(codigo);
        return ResponseEntity.ok(valido);
    }

    /**
     * Endpoint para o aluno efetivamente registrar sua emoção usando um código válido.
     * @PostMapping: Mapeia para requisições HTTP POST, pois está criando um novo recurso (um registro).
     * @param codigo O código que o aluno está usando.
     * @param emocao O valor numérico da emoção que está sendo registrada.
     * @param authentication O token de autenticação para identificar o aluno.
     * @return Um DTO com os detalhes do registro que acabou de ser criado.
     */
    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<RegistroEmocionalDTO> registrar(
            @RequestParam String codigo,
            @RequestParam int emocao,
            OAuth2AuthenticationToken authentication) {

        // Identifica o aluno de forma segura através do ID do Google no token.
        String googleId = (String) authentication.getPrincipal().getAttributes().get("sub");

        // Delega a lógica de negócio (validar, encontrar aluno, salvar registro) para o Service.
        RegistroEmocional registro = registroService.registrarEmocao(googleId, codigo, emocao);

        // Cria um DTO (Data Transfer Object) para enviar uma resposta limpa para o frontend.
        // Isso evita expor o modelo completo do banco de dados e monta um objeto com as
        // informações exatas que a interface do usuário precisa.
        RegistroEmocionalDTO dto = new RegistroEmocionalDTO(
                registro.getId(),
                registro.getEmocao(),
                registro.getCodigoAvaliacaoUsado().getTipo().name(), // Ex: "CHECKIN" ou "CHECKOUT"
                registro.getCodigoAvaliacaoUsado().getDataCriacao().toString(),
                registro.getCodigoAvaliacaoUsado().getProfessor().getNome(),
                registro.getCodigoAvaliacaoUsado().getTurma().getNome()
        );

        // Retorna o DTO com um status HTTP 200 (OK).
        return ResponseEntity.ok(dto);
    }
}