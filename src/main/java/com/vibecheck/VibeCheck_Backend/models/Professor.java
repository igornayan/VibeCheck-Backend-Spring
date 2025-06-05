package com.vibecheck.VibeCheck_Backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professores") // Define o nome da tabela no banco de dados
@Getter
@Setter
@NoArgsConstructor // Construtor padrão (JPA)
@AllArgsConstructor // Construtor com todos os argumentos (Lombok)
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estratégia de ID para PostgreSQL
    private Long id;

    @Column(name = "google_id", nullable = false, unique = true) // ID do Google, único e não nulo
    private String googleId;

    @Column(nullable = false, unique = true) // Email do professor, único e não nulo
    private String email;

    @Column(nullable = false) // Nome do professor, não nulo
    private String nome;
}