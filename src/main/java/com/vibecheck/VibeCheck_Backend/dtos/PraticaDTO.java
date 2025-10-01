// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Este DTO representa uma prática completa com informações do check-in e check-out.
 * 
 * @Data: Esta é uma anotação "atalho" muito poderosa do Lombok. Ela combina a funcionalidade de:
 * - @Getter: Gera todos os getters.
 * - @Setter: Gera todos os setters.
 * - @ToString: Gera um método toString() útil.
 * - @EqualsAndHashCode: Gera os métodos equals() e hashCode().
 * 
 * @AllArgsConstructor: Gera um construtor que aceita todos os campos como argumentos.
 * @NoArgsConstructor: Gera um construtor sem argumentos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PraticaDTO {

    // Identificação da prática
    private Long id;
    private String status; // "ABERTA" ou "FECHADA"
    
    // Informações do aluno
    private Long alunoId;
    private String nomeAluno;
    private String emailAluno;
    
    // Informações da turma
    private Long turmaId;
    private String nomeTurma;
    
    // Timestamps
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String duracaoFormatada;
    private Long duracaoSegundos;
    
    // Emoções registradas
    private Integer emocaoInicial;
    private Integer emocaoFinal;
    
    // Informações dos registros
    private Long checkinRegistroId;
    private Long checkoutRegistroId;
    
    // Informações do professor (da turma)
    private String nomeProfessor;
    
    /**
     * Construtor para criar DTO a partir de uma entidade Pratica.
     * 
     * @param pratica A entidade Pratica para converter.
     */
    public PraticaDTO(com.vibecheck.VibeCheck_Backend.models.Pratica pratica) {
        this.id = pratica.getId();
        this.status = pratica.isAberta() ? "ABERTA" : "FECHADA";
        
        // Informações do aluno
        this.alunoId = pratica.getAluno().getId();
        this.nomeAluno = pratica.getAluno().getNome();
        this.emailAluno = pratica.getAluno().getEmail();
        
        // Informações da turma
        this.turmaId = pratica.getTurma().getId();
        this.nomeTurma = pratica.getTurma().getNome();
        
        // Timestamps
        this.inicio = pratica.getInicio();
        this.fim = pratica.getFim();
        this.duracaoFormatada = pratica.getDuracaoFormatada();
        this.duracaoSegundos = pratica.getDuracaoSegundos();
        
        // Emoções
        this.emocaoInicial = pratica.getEmocaoInicial();
        this.emocaoFinal = pratica.getEmocaoFinal();
        
        // IDs dos registros
        this.checkinRegistroId = pratica.getCheckin() != null ? pratica.getCheckin().getId() : null;
        this.checkoutRegistroId = pratica.getCheckout() != null ? pratica.getCheckout().getId() : null;
        
        // Informações do professor
        this.nomeProfessor = pratica.getTurma().getProfessor() != null ? 
            pratica.getTurma().getProfessor().getNome() : "N/A";
    }
}
