// Define o pacote para as interfaces de repositório.
package com.vibecheck.VibeCheck_Backend.repositories;

// Importações dos modelos e do Spring Data.
import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.models.TipoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Repository: Marca a interface como um componente de repositório do Spring.
 * Estende JpaRepository para herdar métodos CRUD para a entidade RegistroEmocional.
 */
@Repository
public interface RegistroEmocionalRepository extends JpaRepository<RegistroEmocional, Long> {

    // Encontra todos os registros feitos por um objeto Aluno específico.
    // O Spring Data JPA usa o ID do objeto Aluno para criar a consulta.
    List<RegistroEmocional> findByAluno(Aluno aluno);

    // Encontra todos os registros que usaram um objeto CodigoAvaliacao específico.
    List<RegistroEmocional> findByCodigoAvaliacaoUsado(CodigoAvaliacao codigoAvaliacao);

    // Encontra todos os registros de um Aluno que também correspondem a um TipoAvaliacao específico.
    // O 'And' no nome do método combina as duas condições.
    List<RegistroEmocional> findByAlunoAndTipoSubmissao(Aluno aluno, TipoAvaliacao tipoSubmissao);

    /**
     * Encontra registros de um tipo específico que ocorreram dentro de um intervalo de tempo.
     * A palavra-chave 'Between' é interpretada como 'timestamp >= inicio AND timestamp <= fim'.
     * @param tipoSubmissao O tipo de avaliação (CHECKIN ou CHECKOUT).
     * @param inicio A data e hora de início do intervalo.
     * @param fim A data e hora de fim do intervalo.
     * @return Uma lista de registros que atendem aos critérios.
     */
    List<RegistroEmocional> findByTipoSubmissaoAndTimestampBetween(TipoAvaliacao tipoSubmissao, LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca todos os registros e os ordena pelo campo 'timestamp' em ordem decrescente.
     * 'OrderByTimestampDesc' é interpretado para adicionar uma cláusula 'ORDER BY timestamp DESC'
     * à consulta SQL. Útil para exibir um feed de atividades recentes.
     * @return Uma lista de todos os registros, dos mais recentes para os mais antigos.
     */
    List<RegistroEmocional> findAllByOrderByTimestampDesc();
}