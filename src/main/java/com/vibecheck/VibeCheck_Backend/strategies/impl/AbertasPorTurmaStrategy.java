package com.vibecheck.VibeCheck_Backend.strategies.impl;

import com.vibecheck.VibeCheck_Backend.dtos.PraticaResumoDTO;
import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.Pratica;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import com.vibecheck.VibeCheck_Backend.repositories.PraticaRepository;
import com.vibecheck.VibeCheck_Backend.strategies.PraticaListagemInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estratégia para listar práticas abertas de uma turma específica (apenas para professores).
 * Encapsula a lógica de busca de práticas abertas por turma.
 */
@Component
public class AbertasPorTurmaStrategy implements PraticaListagemInterface {
    
    private final PraticaRepository praticaRepository;
    
    @Autowired
    public AbertasPorTurmaStrategy(PraticaRepository praticaRepository) {
        this.praticaRepository = praticaRepository;
    }
    
    @Override
    public List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                          LocalDateTime inicio, LocalDateTime fim, 
                                          OAuth2AuthenticationToken authentication) {
        validarParametros(turma);
        
        List<Pratica> praticas = buscarPraticasAbertasPorTurma(turma);
        
        return praticas.stream()
                .map(PraticaResumoDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Valida os parâmetros necessários para esta estratégia.
     * 
     * @param turma Objeto turma
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    private void validarParametros(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Objeto turma é obrigatório para esta estratégia");
        }
    }
    
    /**
     * Busca práticas abertas de uma turma específica.
     * Encapsula a lógica específica de busca de práticas abertas.
     * 
     * @param turma Objeto turma
     * @return Lista de práticas abertas da turma
     */
    private List<Pratica> buscarPraticasAbertasPorTurma(Turma turma) {
        return praticaRepository.findByTurmaAndCheckoutIsNullOrderByInicioDesc(turma);
    }
    
    @Override
    public TipoEstrategia getTipo() {
        return TipoEstrategia.ABERTAS_POR_TURMA;
    }
}