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
@Table(name = "alunos") // Define o nome da tabela no banco de dados como "alunos"
@Getter
@Setter
@NoArgsConstructor // Construtor padrão (necessário para JPA)
@AllArgsConstructor // Construtor com todos os argumentos
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estratégia de geração de ID para PostgreSQL
    private Long id;

    @Column(name = "google_id", nullable = false, unique = true) // Coluna para o ID do Google
    private String googleId;

    @Column(nullable = false, unique = true) // Coluna para o email, deve ser único e não nulo
    private String email;

    @Column(nullable = false) // Coluna para o nome do aluno
    private String nome;
}