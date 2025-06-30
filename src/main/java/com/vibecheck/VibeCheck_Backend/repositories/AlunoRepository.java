// Define o pacote para as interfaces de repositório.
package com.vibecheck.VibeCheck_Backend.repositories;

// Importações do modelo e do Spring Data.
import com.vibecheck.VibeCheck_Backend.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Repository: Marca esta interface como um componente de repositório do Spring.
 * Facilita a injeção de dependência e a tradução de exceções de persistência.
 */
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    /**
     * Ao estender JpaRepository<Aluno, Long>, esta interface herda automaticamente
     * métodos CRUD (Create, Read, Update, Delete) completos, como:
     * - save(aluno)
     * - findById(id)
     * - findAll()
     * - deleteById(id)
     * ... e muitos outros.
     * Os tipos genéricos <Aluno, Long> especificam que o repositório gerencia a entidade
     * 'Aluno' e que o tipo da chave primária é 'Long'.
     */

    /**
     * Este é um "método de consulta derivado" (derived query method).
     * O Spring Data JPA automaticamente cria a implementação deste método
     * baseado no seu nome. "findByGoogleId" é interpretado como:
     * "Encontre (find) um Aluno pela (By) propriedade 'googleId'".
     *
     * O retorno 'Optional<Aluno>' é uma excelente prática. Ele evita NullPointerExceptions,
     * forçando o código que chama este método a tratar o caso em que nenhum aluno é encontrado.
     *
     * @param googleId O ID do Google a ser procurado.
     * @return Um Optional contendo o Aluno se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Aluno> findByGoogleId(String googleId);
}