// Define o pacote para as interfaces de repositório.
package com.vibecheck.VibeCheck_Backend.repositories;

// Importações dos modelos e do Spring Data.
import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Sugestão: Adicionar a anotação @Repository

import java.util.List;
import java.util.Optional;

// Sugestão: Adicionar @Repository para consistência e clareza.
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    /**
     * Encontra uma turma específica combinando o nome da turma e o objeto Professor.
     * Este método é a contraparte lógica da restrição de chave única composta
     * (@UniqueConstraint) definida na entidade Turma. É usado para verificar se um professor
     * já possui uma turma com um determinado nome.
     * @param nome O nome da turma.
     * @param professor O objeto Professor associado.
     * @return Um Optional contendo a Turma se a combinação exata for encontrada.
     */
    Optional<Turma> findByNomeAndProfessor(String nome, Professor professor);

    /**
     * Encontra todas as turmas que pertencem a um determinado professor.
     * @param professor O objeto Professor cujas turmas serão buscadas.
     * @return Uma lista de turmas pertencentes ao professor.
     */
    List<Turma> findByProfessor(Professor professor);
}