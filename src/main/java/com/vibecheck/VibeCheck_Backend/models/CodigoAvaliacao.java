package com.vibecheck.VibeCheck_Backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "codigos_avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodigoAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6) // O código de 6 caracteres
    private String codigo;

    @Enumerated(EnumType.STRING) // Grava o nome do enum (CHECKIN/CHECKOUT) como String no BD
    @Column(nullable = false)
    private TipoAvaliacao tipo;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos códigos podem pertencer a um professor
    @JoinColumn(name = "professor_id", nullable = false) // Chave estrangeira para a tabela de professores
    private Professor professor;
}