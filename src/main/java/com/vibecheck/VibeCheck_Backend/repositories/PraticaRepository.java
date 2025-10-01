// Define o pacote para as interfaces de repositório.
package com.vibecheck.VibeCheck_Backend.repositories;

// Importações dos modelos e do Spring Data.
import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.Pratica;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @Repository: Marca a interface como um componente de repositório do Spring.
 * Estende JpaRepository para herdar métodos CRUD para a entidade Pratica.
 */
@Repository
public interface PraticaRepository extends JpaRepository<Pratica, Long> {

    /**
     * Encontra a prática aberta mais recente de um aluno em uma turma específica.
     * Uma prática aberta é aquela que tem check-in mas não tem check-out.
     * 
     * @param aluno O aluno para buscar a prática aberta.
     * @param turma A turma para buscar a prática aberta.
     * @return Optional contendo a prática aberta mais recente, ou vazio se não houver.
     */
    Optional<Pratica> findFirstByAlunoAndTurmaAndCheckoutIsNullOrderByInicioDesc(Aluno aluno, Turma turma);

    /**
     * Encontra todas as práticas de um aluno específico.
     * 
     * @param aluno O aluno para buscar as práticas.
     * @return Lista de práticas do aluno, ordenadas por data de início (mais recente primeiro).
     */
    List<Pratica> findByAlunoOrderByInicioDesc(Aluno aluno);

    /**
     * Encontra todas as práticas de uma turma específica.
     * 
     * @param turma A turma para buscar as práticas.
     * @return Lista de práticas da turma, ordenadas por data de início (mais recente primeiro).
     */
    List<Pratica> findByTurmaOrderByInicioDesc(Turma turma);

    /**
     * Encontra práticas de um aluno em um período específico.
     * 
     * @param aluno O aluno para buscar as práticas.
     * @param inicio Data de início do período.
     * @param fim Data de fim do período.
     * @return Lista de práticas do aluno no período especificado.
     */
    List<Pratica> findByAlunoAndInicioBetween(Aluno aluno, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Encontra práticas de uma turma em um período específico.
     * 
     * @param turma A turma para buscar as práticas.
     * @param inicio Data de início do período.
     * @param fim Data de fim do período.
     * @return Lista de práticas da turma no período especificado.
     */
    List<Pratica> findByTurmaAndInicioBetween(Turma turma, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Encontra todas as práticas abertas (sem check-out) de uma turma.
     * 
     * @param turma A turma para buscar as práticas abertas.
     * @return Lista de práticas abertas da turma.
     */
    List<Pratica> findByTurmaAndCheckoutIsNullOrderByInicioDesc(Turma turma);

    /**
     * Encontra todas as práticas abertas (sem check-out) de um aluno.
     * 
     * @param aluno O aluno para buscar as práticas abertas.
     * @return Lista de práticas abertas do aluno.
     */
    List<Pratica> findByAlunoAndCheckoutIsNullOrderByInicioDesc(Aluno aluno);

    /**
     * Consulta personalizada para buscar práticas com informações detalhadas do aluno e turma.
     * 
     * @param alunoId ID do aluno.
     * @return Lista de práticas com dados do aluno e turma carregados.
     */
    @Query("SELECT p FROM Pratica p " +
           "LEFT JOIN FETCH p.aluno a " +
           "LEFT JOIN FETCH p.turma t " +
           "WHERE a.id = :alunoId " +
           "ORDER BY p.inicio DESC")
    List<Pratica> findByAlunoWithDetails(@Param("alunoId") Long alunoId);

    /**
     * Consulta personalizada para buscar práticas de uma turma com informações detalhadas.
     * 
     * @param turmaId ID da turma.
     * @return Lista de práticas com dados do aluno e turma carregados.
     */
    @Query("SELECT p FROM Pratica p " +
           "LEFT JOIN FETCH p.aluno a " +
           "LEFT JOIN FETCH p.turma t " +
           "WHERE t.id = :turmaId " +
           "ORDER BY p.inicio DESC")
    List<Pratica> findByTurmaWithDetails(@Param("turmaId") Long turmaId);
}

