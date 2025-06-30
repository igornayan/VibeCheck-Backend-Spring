// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Este DTO representa a resposta completa que o aluno recebe após registrar com sucesso sua emoção.
 *
 * @Data: Esta é uma anotação "atalho" muito poderosa do Lombok. Ela combina a funcionalidade de:
 * - @Getter: Gera todos os getters.
 * - @Setter: Gera todos os setters.
 * - @ToString: Gera um método toString() útil.
 * - @EqualsAndHashCode: Gera os métodos equals() e hashCode().
 * - @RequiredArgsConstructor: Gera um construtor para campos 'final' (não aplicável aqui).
 *
 * @AllArgsConstructor: Gera um construtor que aceita todos os campos como argumentos.
 */
@Data
@AllArgsConstructor
public class RegistroEmocionalDTO {

    // Os campos que compõem a resposta, agregando dados de várias entidades.
    private Long id;              // O ID do registro emocional criado.
    private int emocao;           // A emoção que o aluno registrou.
    private String tipoCodigo;    // O tipo de código usado: "CHECKIN" ou "CHECKOUT".
    private String dataCriacao;   // A data de criação do código de avaliação usado.
    private String nomeProfessor; // O nome do professor da turma.
    private String nomeTurma;     // O nome da turma.
}