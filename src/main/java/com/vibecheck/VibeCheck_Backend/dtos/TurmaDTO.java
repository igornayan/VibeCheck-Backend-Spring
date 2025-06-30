// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Este DTO (Data Transfer Object) representa uma Turma de forma simplificada,
 * contendo apenas seu ID e nome. É ideal para ser usado em listas ou respostas de API.
 *
 * @Getter: Gera os métodos getters para os campos (getId(), getNome()).
 * @Setter: Gera os métodos setters para os campos (setId(), setNome()).
 * @AllArgsConstructor: Gera um construtor que aceita todos os campos como argumentos.
 */
@Getter
@Setter
@AllArgsConstructor
public class TurmaDTO {

    // O identificador único da turma.
    private Long id;
    // O nome da turma.
    private String nome;
}