package com.vibecheck.VibeCheck_Backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "turmas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nome", "professor_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodigoAvaliacao> codigos; // Opcional: se quiser acessar os códigos por turma
}
