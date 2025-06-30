// Define o pacote para as classes de modelo (entidades do banco de dados).
package com.vibecheck.VibeCheck_Backend.models;

// Importações do Jakarta Persistence (JPA), a especificação padrão do Java para persistência de dados.
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// Importações do Lombok para reduzir código repetitivo.
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Entity: Anotação fundamental que marca esta classe como uma entidade JPA.
 * Isso informa ao provedor de persistência (como o Hibernate) que esta classe
 * corresponde a uma tabela no banco de dados.
 *
 * @Table(name = "alunos"): Especifica o nome exato da tabela no banco de dados.
 * Se omitido, o JPA usaria o nome da classe ("aluno") por padrão.
 *
 * @Getter/@Setter: Geram os métodos get/set para todos os campos.
 * @NoArgsConstructor: Gera um construtor vazio. É um requisito obrigatório para entidades JPA.
 * @AllArgsConstructor: Gera um construtor com todos os campos como argumentos.
 */
@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    /**
     * @Id: Marca este campo como a chave primária (Primary Key) da tabela.
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Configura a estratégia de geração da chave primária.
     * 'IDENTITY' delega a geração do ID para o próprio banco de dados (ex: usando colunas auto-incrementáveis
     * como SERIAL ou BIGSERIAL no PostgreSQL). É a escolha ideal para a maioria dos casos com PostgreSQL/MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(...): Mapeia este campo para uma coluna específica na tabela.
     * name = "google_id": Define o nome da coluna no banco como "google_id" (padrão snake_case).
     * nullable = false: Adiciona uma restrição NOT NULL na coluna do banco.
     * unique = true: Adiciona uma restrição UNIQUE, garantindo que não haja dois alunos com o mesmo Google ID.
     */
    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    /**
     * Mapeia o campo 'email' para uma coluna 'email' na tabela.
     * Também garante que o email não pode ser nulo e deve ser único para cada aluno.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Mapeia o campo 'nome' para uma coluna 'nome' na tabela.
     * O nome não pode ser nulo.
     */
    @Column(nullable = false)
    private String nome;
}