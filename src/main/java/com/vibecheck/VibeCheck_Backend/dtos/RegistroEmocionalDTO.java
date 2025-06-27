package com.vibecheck.VibeCheck_Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistroEmocionalDTO {
    private Long id;
    private int emocao;
    private String tipoCodigo; // "CHECKIN" ou "CHECKOUT"
    private String dataCriacao;
    private String nomeProfessor;
    private String nomeTurma;
}
