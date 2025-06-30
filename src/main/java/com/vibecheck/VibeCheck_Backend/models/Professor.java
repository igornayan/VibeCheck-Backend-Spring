// Define o pacote para as classes de modelo (entidades do banco de dados).
package com.vibecheck.VibeCheck_Backend.models;

// Importações do Jakarta Persistence (JPA) e Lombok.
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

/**
 * @Entity: Marca esta classe como uma entidade JPA, vinculando-a a uma tabela no banco.
 * @Table(name = "professores"): Define o nome exato da tabela no banco de dados.
 * @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor: Anotações Lombok para gerar código padrão.
 */
@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor {

    /**
     * @Id: Define este campo como a chave primária da tabela.
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Configura o banco de dados (ex: PostgreSQL)
     * para gerar e gerenciar o valor do ID automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(...): Mapeia o campo para uma coluna com restrições específicas.
     * name = "google_id": Define o nome da coluna.
     * nullable = false: A coluna não pode ser nula.
     * unique = true: Cada professor deve ter um Google ID único.
     */
    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    /**
     * O email do professor também não pode ser nulo e deve ser único na tabela.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * O nome do professor não pode ser nulo.
     */
    @Column(nullable = false)
    private String nome;
}