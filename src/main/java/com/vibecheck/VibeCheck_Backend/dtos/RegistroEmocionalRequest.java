package com.vibecheck.VibeCheck_Backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroEmocionalRequest {
    private String googleId;
    private String codigo;
    private int emocao;
}
