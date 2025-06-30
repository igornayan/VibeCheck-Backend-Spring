// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do Lombok.
import lombok.Getter;
import lombok.Setter;

/**
 * Este DTO (Data Transfer Object) foi projetado para modelar a requisição
 * de um aluno ao registrar uma emoção.
 *
 * @Getter: Gera os métodos getters para os campos.
 * @Setter: Gera os métodos setters, permitindo que o Spring popule o objeto
 * a partir de um JSON.
 */
@Getter
@Setter
public class RegistroEmocionalRequest {

    // ATENÇÃO: Incluir o ID do usuário em uma requisição é uma falha de segurança.
    private String googleId;
    private String codigo;
    private int emocao;
}