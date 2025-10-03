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
 * Estratégia para listar práticas de uma turma específica (apenas para professores).
 * Encapsula a lógica de busca de práticas por turma.
 */
@Component
public class PorTurmaStrategy implements PraticaListagemInterface {
    
    private final PraticaRepository praticaRepository;
    
    @Autowired
    public PorTurmaStrategy(PraticaRepository praticaRepository) {
        this.praticaRepository = praticaRepository;
    }
    
    @Override
    public List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                          LocalDateTime inicio, LocalDateTime fim, 
                                          OAuth2AuthenticationToken authentication) {
        validarParametros(turmaId);
        
        List<Pratica> praticas = buscarPraticasPorTurma(turmaId);
        
        return praticas.stream()
                .map(PraticaResumoDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Valida os parâmetros necessários para esta estratégia.
     * 
     * @param turmaId ID da turma
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    private void validarParametros(Long turmaId) {
        if (turmaId == null) {
            throw new IllegalArgumentException("ID da turma é obrigatório para esta estratégia");
        }
    }
    
    /**
     * Busca práticas de uma turma específica.
     * Encapsula a lógica específica de busca de práticas por turma.
     * 
     * @param turmaId ID da turma
     * @return Lista de práticas da turma
     */
    private List<Pratica> buscarPraticasPorTurma(Long turmaId) {
        return praticaRepository.findByTurmaWithDetails(turmaId);
    }
    
    @Override
    public TipoEstrategia getTipo() {
        return TipoEstrategia.POR_TURMA;
    }
}