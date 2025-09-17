package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import com.vibecheck.VibeCheck_Backend.repositories.ProfessorRepository;
import com.vibecheck.VibeCheck_Backend.repositories.TurmaRepository;
import com.vibecheck.VibeCheck_Backend.dtos.TurmaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Cacheable(value = "turmasPorProfessor", key = "#googleId") // Cache a lista de turmas por professor
    public List<TurmaDTO> listarTurmasComIdPorProfessor(String googleId) {
        // Encontra o professor pelo seu ID do Google.
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        // Busca todas as turmas associadas a esse professor.
        List<Turma> turmas = turmaRepository.findByProfessor(professor);

        // Transforma a lista de entidades 'Turma' em uma lista de 'TurmaDTO'.
        return turmas.stream()
                .map(t -> new TurmaDTO(t.getId(), t.getNome())) // Cria DTO para cada turma
                .collect(Collectors.toList());
    }

    /**
     * Lista apenas os nomes das turmas de um professor.
     * Útil para exibições mais simples.
     */
    @Cacheable(value = "turmasPorProfessor", key = "#googleId") // Cache a lista de nomes de turmas
    public List<String> listarNomesPorProfessor(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // Retorna apenas os nomes das turmas associadas ao professor.
        return turmaRepository.findByProfessor(professor)
                .stream()
                .map(Turma::getNome) // Extrai apenas o nome de cada turma.
                .collect(Collectors.toList());
    }

    /**
     * Atualiza o nome de uma turma existente.
     *
     * @param id       ID da turma.
     * @param novoNome Novo nome da turma.
     */
    @CacheEvict(value = "turmasPorProfessor", key = "#googleId") // Limpa o cache quando o nome da turma for atualizado
    public void atualizarNomeTurma(Long id, String novoNome) {
        // Busca a turma pelo ID; se não encontrar, lança uma exceção.
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // Altera o nome no objeto em memória.
        turma.setNome(novoNome);
        // Salva a turma. O JPA detecta que a entidade já existe e executa um UPDATE.
        turmaRepository.save(turma);
    }

    /**
     * Exclui uma turma, mas apenas se não houver registros emocionais associados.
     * @param id ID da turma a ser excluída.
     */
    @CacheEvict(value = "turmasPorProfessor", key = "#googleId") // Limpa o cache ao excluir a turma
    public void excluirTurma(Long id) {
        // Busca a turma pelo ID; se não encontrar, lança uma exceção.
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        // REGRA DE NEGÓCIO CRÍTICA: Verifica se algum código da turma já foi usado.
        // Itera pelos códigos da turma, e para cada código, verifica se a lista de registros não está vazia.
        if (turma.getCodigos().stream().anyMatch(c -> !c.getRegistros().isEmpty())) {
            // Se encontrar qualquer registro, impede a exclusão para preservar os dados históricos.
            throw new RuntimeException("A turma possui registros emocionais associados e não pode ser excluída.");
        }

        // Se a verificação passar, a turma é excluída.
        turmaRepository.delete(turma);
    }

    /**
     * Método para adicionar uma nova turma.
     * @param googleId O ID do professor que cria a turma.
     * @param nomeTurma O nome da nova turma.
     */
    @CacheEvict(value = "turmasPorProfessor", key = "#googleId") // Limpa o cache ao adicionar uma nova turma
    public void adicionarTurma(String googleId, String nomeTurma) {
        // Encontra o professor pelo ID do Google.
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        // Cria a nova turma e associa o professor.
        Turma novaTurma = new Turma();
        novaTurma.setNome(nomeTurma);
        novaTurma.setProfessor(professor);

        // Salva a nova turma no banco de dados.
        turmaRepository.save(novaTurma);
    }
}
