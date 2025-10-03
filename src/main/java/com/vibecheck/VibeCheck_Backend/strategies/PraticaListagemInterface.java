package com.vibecheck.VibeCheck_Backend.strategies;

import com.vibecheck.VibeCheck_Backend.dtos.PraticaResumoDTO;
import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface Strategy para diferentes estratégias de listagem de práticas.
 * Encapsula as diferentes formas de buscar e listar práticas no sistema.
 */
public interface PraticaListagemInterface {
    
    /**
     * Executa a estratégia de listagem de práticas.
     * 
     * @param turmaId ID da turma (pode ser null dependendo da estratégia)
     * @param turma Objeto turma (pode ser null dependendo da estratégia)
     * @param aluno Objeto aluno (pode ser null dependendo da estratégia)
     * @param inicio Data de início (pode ser null dependendo da estratégia)
     * @param fim Data de fim (pode ser null dependendo da estratégia)
     * @param authentication Token de autenticação (pode ser null dependendo da estratégia)
     * @return Lista de práticas resumidas
     */
    List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                   LocalDateTime inicio, LocalDateTime fim, 
                                   OAuth2AuthenticationToken authentication);
    
    /**
     * Retorna o tipo de estratégia para identificação.
     * 
     * @return Tipo da estratégia
     */
    TipoEstrategia getTipo();
    
    /**
     * Enum para identificar os tipos de estratégias disponíveis.
     */
    enum TipoEstrategia {
        TODAS_PRATICAS,
        POR_TURMA,
        ABERTAS_POR_TURMA,
        MINHAS_ABERTAS,
        POR_TURMA_PERIODO
    }
}

