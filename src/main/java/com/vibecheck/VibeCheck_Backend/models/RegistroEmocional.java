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
@Table(name = "registros_emocionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEmocional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer emocao; // Representa a emoção (ex: um número mapeado para um emoji/estado)

    @Column(nullable = false)
    private LocalDateTime timestamp; // Quando o registro foi feito

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_submissao", nullable = false) // CHECKIN ou CHECKOUT
    private TipoAvaliacao tipoSubmissao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false) // Quem submeteu
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_avaliacao_id", nullable = false) // Qual código foi usado
    private CodigoAvaliacao codigoAvaliacaoUsado;
}