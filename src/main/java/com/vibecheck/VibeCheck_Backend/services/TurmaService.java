package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import com.vibecheck.VibeCheck_Backend.repositories.ProfessorRepository;
import com.vibecheck.VibeCheck_Backend.repositories.TurmaRepository;
import com.vibecheck.VibeCheck_Backend.dtos.TurmaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;

    public List<TurmaDTO> listarTurmasComIdPorProfessor(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        List<Turma> turmas = turmaRepository.findByProfessor(professor);

        return turmas.stream()
                .map(t -> new TurmaDTO(t.getId(), t.getNome()))
                .collect(Collectors.toList());
    }

    @Autowired
    public TurmaService(TurmaRepository turmaRepository, ProfessorRepository professorRepository) {
        this.turmaRepository = turmaRepository;
        this.professorRepository = professorRepository;
    }

    public List<String> listarNomesPorProfessor(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        return turmaRepository.findByProfessor(professor)
                .stream()
                .map(Turma::getNome)
                .collect(Collectors.toList());
    }

    public void atualizarNomeTurma(Long id, String novoNome) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        turma.setNome(novoNome);
        turmaRepository.save(turma);
    }

    public void excluirTurma(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new RuntimeException("Turma não encontrada");
        }

        turmaRepository.deleteById(id);
    }
}
