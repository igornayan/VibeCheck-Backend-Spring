// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.Getter;
import lombok.Setter;

/**
 * Este DTO (Data Transfer Object) define o formato do corpo (body) da requisição
 * que o cliente (frontend) deve enviar ao solicitar a liberação de um novo código
 * de check-in ou check-out.
 *
 * @Getter: Gera o método getNomeTurma().
 * @Setter: Gera o método setNomeTurma(String nomeTurma). Essencial para que o Spring
 * consiga popular este objeto a partir de um JSON.
 */
@Getter
@Setter
public class LiberarCodigoRequest {

    // O único campo necessário na requisição: o nome da turma para a qual o código será gerado.
    private String nomeTurma;
}