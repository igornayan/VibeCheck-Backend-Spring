package com.vibecheck.VibeCheck_Backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardRegistroDTO {
    private String data;
    private int emocao;
    private String tipo;
    private String turma;
}
