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
 * Estratégia para listar todas as práticas (apenas para professores).
 * Encapsula a lógica de busca de todas as práticas do sistema.
 * TODO: Implementar busca por todas as turmas do professor.
 */
@Component
public class TodasPraticasStrategy implements PraticaListagemInterface {
    
    private final PraticaRepository praticaRepository;
    
    @Autowired
    public TodasPraticasStrategy(PraticaRepository praticaRepository) {
        this.praticaRepository = praticaRepository;
    }
    
    @Override
    public List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                          LocalDateTime inicio, LocalDateTime fim, 
                                          OAuth2AuthenticationToken authentication) {
        List<Pratica> praticas = buscarTodasPraticas();
        
        return praticas.stream()
                .map(PraticaResumoDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca todas as práticas do sistema.
     * Encapsula a lógica específica de busca de todas as práticas.
     * TODO: Implementar busca por todas as turmas do professor autenticado.
     * 
     * @return Lista de todas as práticas
     */
    private List<Pratica> buscarTodasPraticas() {
        // Por enquanto, busca pela turma com ID 1 como fallback
        // Futuramente, deve buscar por todas as turmas do professor autenticado
        return praticaRepository.findByTurmaWithDetails(1L);
    }
    
    @Override
    public TipoEstrategia getTipo() {
        return TipoEstrategia.TODAS_PRATICAS;
    }
}