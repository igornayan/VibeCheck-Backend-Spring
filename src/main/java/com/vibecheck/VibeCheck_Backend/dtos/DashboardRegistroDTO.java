// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Este DTO representa uma única entrada de dados para o dashboard. Ele agrega
 * informações de um registro emocional de forma simplificada para fácil consumo pelo frontend.
 *
 * @Getter: Anotação do Lombok que gera os métodos getters para todos os campos.
 * @Setter: Anotação do Lombok que gera os métodos setters para todos os campos.
 * @AllArgsConstructor: Anotação do Lombok que gera um construtor com todos os campos como argumentos.
 */
@Getter
@Setter
@AllArgsConstructor
public class DashboardRegistroDTO {

    // Os campos que compõem a estrutura de dados deste DTO.
    private String data;    // A data em que o registro foi feito.
    private int emocao;     // O valor da emoção registrada.
    private String tipo;    // O tipo de registro (ex: "CHECKIN" ou "CHECKOUT").
    private String turma;   // O nome da turma associada ao registro.
}