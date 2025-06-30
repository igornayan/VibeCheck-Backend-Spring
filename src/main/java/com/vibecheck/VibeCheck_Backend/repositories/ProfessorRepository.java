// Define o pacote para as interfaces de repositório.
package com.vibecheck.VibeCheck_Backend.repositories;

// Importações do modelo e do Spring Data.
import com.vibecheck.VibeCheck_Backend.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Repository: Marca a interface como um componente de repositório do Spring.
 * Estende JpaRepository<Professor, Long> para herdar métodos CRUD para a entidade
 * 'Professor', cuja chave primária é do tipo 'Long'.
 */
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    /**
     * Método de consulta derivado para buscar um Professor pelo seu googleId.
     * O Spring Data JPA gera a consulta SQL correspondente automaticamente.
     *
     * O retorno 'Optional<Professor>' é uma boa prática para evitar NullPointerExceptions.
     * @param googleId O ID do Google a ser procurado.
     * @return Um Optional contendo o Professor se encontrado, ou um Optional vazio.
     */
    Optional<Professor> findByGoogleId(String googleId);
}