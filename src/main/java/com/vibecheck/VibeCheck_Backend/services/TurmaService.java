// Define o pacote para as classes de serviço.
package com.vibecheck.VibeCheck_Backend.services;

// Importações dos modelos, repositórios e DTOs.
import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import com.vibecheck.VibeCheck_Backend.repositories.ProfessorRepository;
import com.vibecheck.VibeCheck_Backend.repositories.TurmaRepository;
import com.vibecheck.VibeCheck_Backend.dtos.TurmaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Sugestão: Usar transações.

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Service: Marca a classe como um componente de serviço do Spring.
 */
@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public TurmaService(TurmaRepository turmaRepository, ProfessorRepository professorRepository) {
        this.turmaRepository = turmaRepository;
        this.professorRepository = professorRepository;
    }

    /**
     * Lista as turmas de um professor, retornando DTOs que incluem o ID e o nome.
     * Ideal para preencher uma lista no frontend onde cada item precisa ser identificável.
     */
    public List<TurmaDTO> listarTurmasComIdPorProfessor(String googleId) {
        // 1. Encontra o professor pelo seu ID do Google.
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        // 2. Busca todas as turmas associadas a esse professor.
        List<Turma> turmas = turmaRepository.findByProfessor(professor);

        // 3. Transforma a lista de entidades 'Turma' em uma lista de 'TurmaDTO'.
        return turmas.stream()
                .map(t -> new TurmaDTO(t.getId(), t.getNome()))
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas os nomes das turmas de um professor.
     * Útil para exibições mais simples.
     */
    public List<String> listarNomesPorProfessor(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        return turmaRepository.findByProfessor(professor)
                .stream()
                .map(Turma::getNome) // Extrai apenas o nome de cada turma.
                .collect(Collectors.toList());
    }

    /**
     * Atualiza o nome de uma turma existente.
     * Sugestão: Adicionar @Transactional para garantir a atomicidade da operação.
     */
    // @Transactional
    public void atualizarNomeTurma(Long id, String novoNome) {
        // Busca a turma pelo ID; se não encontrar, lança uma exceção.
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // Altera o nome no objeto em memória.
        turma.setNome(novoNome);
        // Salva o objeto. O JPA detecta que a entidade já existe e executa um UPDATE.
        turmaRepository.save(turma);
    }

    /**
     * Exclui uma turma, mas apenas se não houver registros emocionais associados.
     * Sugestão: Adicionar @Transactional.
     */
    // @Transactional
    public void excluirTurma(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        // REGRA DE NEGÓCIO CRÍTICA: Verifica se algum código da turma já foi usado.
        // Itera pelos códigos da turma, e para cada código, verifica se a lista de registros não está vazia.
        if (turma.getCodigos().stream().anyMatch(c -> !c.getRegistros().isEmpty())) {
            // Se encontrar qualquer registro, impede a exclusão para preservar os dados históricos.
            throw new RuntimeException("A turma possui registros emocionais associados e não pode ser excluída.");
        }

        // Se a verificação passar, a turma é excluída.
        // Graças ao 'cascade' na entidade Turma, os códigos associados também serão excluídos.
        turmaRepository.delete(turma);
    }
}