// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Este DTO representa um resumo de uma prática para listagens e dashboards.
 * Contém apenas as informações essenciais para exibição em listas.
 * 
 * @Data: Esta é uma anotação "atalho" muito poderosa do Lombok.
 * @AllArgsConstructor: Gera um construtor que aceita todos os campos como argumentos.
 * @NoArgsConstructor: Gera um construtor sem argumentos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PraticaResumoDTO {

    // Identificação básica
    private Long id;
    private String status; // "ABERTA" ou "FECHADA"
    
    // Informações essenciais
    private String nomeAluno;
    private String nomeTurma;
    private String nomeProfessor;
    
    // Timestamps
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String duracaoFormatada;
    
    // Emoções (resumo)
    private Integer emocaoInicial;
    private Integer emocaoFinal;
    
    /**
     * Construtor para criar DTO a partir de uma entidade Pratica.
     * 
     * @param pratica A entidade Pratica para converter.
     */
    public PraticaResumoDTO(com.vibecheck.VibeCheck_Backend.models.Pratica pratica) {
        this.id = pratica.getId();
        this.status = pratica.isAberta() ? "ABERTA" : "FECHADA";
        
        // Informações essenciais
        this.nomeAluno = pratica.getAluno().getNome();
        this.nomeTurma = pratica.getTurma().getNome();
        this.nomeProfessor = pratica.getTurma().getProfessor() != null ? 
            pratica.getTurma().getProfessor().getNome() : "N/A";
        
        // Timestamps
        this.inicio = pratica.getInicio();
        this.fim = pratica.getFim();
        this.duracaoFormatada = pratica.getDuracaoFormatada();
        
        // Emoções
        this.emocaoInicial = pratica.getEmocaoInicial();
        this.emocaoFinal = pratica.getEmocaoFinal();
    }
}
