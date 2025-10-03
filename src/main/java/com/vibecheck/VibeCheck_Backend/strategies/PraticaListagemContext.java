package com.vibecheck.VibeCheck_Backend.strategies;

import com.vibecheck.VibeCheck_Backend.dtos.PraticaResumoDTO;
import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import com.vibecheck.VibeCheck_Backend.strategies.PraticaListagemInterface.TipoEstrategia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Context class para gerenciar as estratégias de listagem de práticas.
 * Implementa o padrão Strategy para encapsular diferentes formas de listar práticas.
 */
@Component
public class PraticaListagemContext {
    
    private final Map<TipoEstrategia, PraticaListagemInterface> estrategias;
    
    @Autowired
    public PraticaListagemContext(List<PraticaListagemInterface> strategyList) {
        this.estrategias = new HashMap<>();
        
        // Registra todas as estratégias disponíveis
        for (PraticaListagemInterface strategy : strategyList) {
            estrategias.put(strategy.getTipo(), strategy);
        }
    }
    
    /**
     * Executa a estratégia de listagem de práticas especificada.
     * 
     * @param tipoEstrategia Tipo da estratégia a ser executada
     * @param turmaId ID da turma (pode ser null dependendo da estratégia)
     * @param turma Objeto turma (pode ser null dependendo da estratégia)
     * @param aluno Objeto aluno (pode ser null dependendo da estratégia)
     * @param inicio Data de início (pode ser null dependendo da estratégia)
     * @param fim Data de fim (pode ser null dependendo da estratégia)
     * @param authentication Token de autenticação (pode ser null dependendo da estratégia)
     * @return Lista de práticas resumidas
     * @throws IllegalArgumentException se a estratégia não for encontrada
     */
    public List<PraticaResumoDTO> executarEstrategia(TipoEstrategia tipoEstrategia, 
                                                    Long turmaId, 
                                                    Turma turma, 
                                                    Aluno aluno, 
                                                    LocalDateTime inicio, 
                                                    LocalDateTime fim, 
                                                    OAuth2AuthenticationToken authentication) {
        PraticaListagemInterface strategy = estrategias.get(tipoEstrategia);
        
        if (strategy == null) {
            throw new IllegalArgumentException("Estratégia não encontrada: " + tipoEstrategia);
        }
        
        return strategy.executar(turmaId, turma, aluno, inicio, fim, authentication);
    }
    
    /**
     * Verifica se uma estratégia está disponível.
     * 
     * @param tipoEstrategia Tipo da estratégia
     * @return true se a estratégia estiver disponível, false caso contrário
     */
    public boolean isEstrategiaDisponivel(TipoEstrategia tipoEstrategia) {
        return estrategias.containsKey(tipoEstrategia);
    }
    
    /**
     * Retorna todas as estratégias disponíveis.
     * 
     * @return Map com todas as estratégias registradas
     */
    public Map<TipoEstrategia, PraticaListagemInterface> getEstrategiasDisponiveis() {
        return new HashMap<>(estrategias);
    }
}
