package com.vibecheck.VibeCheck_Backend.dtos;

import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodigoAvaliacaoResponseDTO {

    private String codigo;
    private String tipo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;

    public CodigoAvaliacaoResponseDTO(CodigoAvaliacao codigoAvaliacao) {
        this.codigo = codigoAvaliacao.getCodigo();
        this.tipo = codigoAvaliacao.getTipo().toString();
        this.dataCriacao = codigoAvaliacao.getDataCriacao();
        this.dataExpiracao = codigoAvaliacao.getDataExpiracao();
    }
}