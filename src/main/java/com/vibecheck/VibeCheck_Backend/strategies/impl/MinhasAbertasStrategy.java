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
 * Estratégia para listar práticas abertas do aluno autenticado (apenas para alunos).
 * Encapsula a lógica de busca de práticas abertas por aluno.
 */
@Component
public class MinhasAbertasStrategy implements PraticaListagemInterface {
    
    private final PraticaRepository praticaRepository;
    
    @Autowired
    public MinhasAbertasStrategy(PraticaRepository praticaRepository) {
        this.praticaRepository = praticaRepository;
    }
    
    @Override
    public List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                          LocalDateTime inicio, LocalDateTime fim, 
                                          OAuth2AuthenticationToken authentication) {
        validarParametros(aluno);
        
        List<Pratica> praticas = buscarPraticasAbertasPorAluno(aluno);
        
        return praticas.stream()
                .map(PraticaResumoDTO::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Valida os parâmetros necessários para esta estratégia.
     * 
     * @param aluno Objeto aluno
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    private void validarParametros(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Objeto aluno é obrigatório para esta estratégia");
        }
    }
    
    /**
     * Busca práticas abertas de um aluno específico.
     * Encapsula a lógica específica de busca de práticas abertas por aluno.
     * 
     * @param aluno Objeto aluno
     * @return Lista de práticas abertas do aluno
     */
    private List<Pratica> buscarPraticasAbertasPorAluno(Aluno aluno) {
        return praticaRepository.findByAlunoAndCheckoutIsNullOrderByInicioDesc(aluno);
    }
    
    @Override
    public TipoEstrategia getTipo() {
        return TipoEstrategia.MINHAS_ABERTAS;
    }
}