// Define o pacote onde a classe DTO está localizada.
package com.vibecheck.VibeCheck_Backend.dtos;

// Importações do modelo e do Lombok.
import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Esta é uma classe DTO (Data Transfer Object) usada para formatar a resposta da API
 * ao enviar dados de um CodigoAvaliacao.
 *
 * @Getter: Anotação do Lombok que gera automaticamente os métodos getters para todos os campos
 * (ex: getCodigo(), getTipo(), etc.) em tempo de compilação.
 *
 * @NoArgsConstructor: Gera um construtor sem argumentos. É útil para frameworks como o Jackson (que converte objetos para JSON)
 * poderem instanciar o objeto.
 *
 * @AllArgsConstructor: Gera um construtor que aceita um argumento para cada campo da classe.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodigoAvaliacaoResponseDTO {

    // Define os campos que farão parte do objeto JSON de resposta.
    private String codigo;
    private String tipo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;

    /**
     * Este é um "construtor de mapeamento". É uma prática muito comum e eficiente.
     * Ele permite criar um DTO diretamente a partir de um objeto da entidade do banco de dados (CodigoAvaliacao),
     * fazendo a conversão necessária dos campos.
     *
     * @param codigoAvaliacao O objeto da entidade que será mapeado para este DTO.
     */
    public CodigoAvaliacaoResponseDTO(CodigoAvaliacao codigoAvaliacao) {
        this.codigo = codigoAvaliacao.getCodigo();
        // Converte o tipo Enum da entidade para uma String, que é um formato mais universal para JSON.
        this.tipo = codigoAvaliacao.getTipo().toString();
        this.dataCriacao = codigoAvaliacao.getDataCriacao();
        this.dataExpiracao = codigoAvaliacao.getDataExpiracao();
    }
}