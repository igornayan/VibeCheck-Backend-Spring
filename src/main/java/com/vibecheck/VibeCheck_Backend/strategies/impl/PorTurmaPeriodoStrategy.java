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
 * Estratégia para listar práticas de uma turma em um período específico (apenas para professores).
 * Encapsula a lógica de busca de práticas por turma e período.
 */
@Component
public class PorTurmaPeriodoStrategy implements PraticaListagemInterface {
    
    private final PraticaRepository praticaRepository;
    
    @Autowired
    public PorTurmaPeriodoStrategy(PraticaRepository praticaRepository) {
        this.praticaRepository = praticaRepository;
    }
    
    @Override
    public List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                          LocalDateTime inicio, LocalDateTime fim, 
                                          OAuth2AuthenticationToken authentication) {
        validarParametros(turma, inicio, fim);
        
        List<Pratica> praticas = buscarPraticasPorTurmaEPeriodo(turma, inicio, fim);
        
        return praticas.stream()
                .map(PraticaResumoDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Valida os parâmetros necessários para esta estratégia.
     * 
     * @param turma Objeto turma
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    private void validarParametros(Turma turma, LocalDateTime inicio, LocalDateTime fim) {
        if (turma == null) {
            throw new IllegalArgumentException("Objeto turma é obrigatório para esta estratégia");
        }
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Data de início e fim são obrigatórias para esta estratégia");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
    }
    
    /**
     * Busca práticas de uma turma em um período específico.
     * Encapsula a lógica específica de busca de práticas por turma e período.
     * 
     * @param turma Objeto turma
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Lista de práticas da turma no período
     */
    private List<Pratica> buscarPraticasPorTurmaEPeriodo(Turma turma, LocalDateTime inicio, LocalDateTime fim) {
        return praticaRepository.findByTurmaAndInicioBetween(turma, inicio, fim);
    }
    
    @Override
    public TipoEstrategia getTipo() {
        return TipoEstrategia.POR_TURMA_PERIODO;
    }
}