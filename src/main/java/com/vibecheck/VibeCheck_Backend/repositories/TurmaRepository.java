package com.vibecheck.VibeCheck_Backend.repositories;

import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.models.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Optional<Turma> findByNomeAndProfessor(String nome, Professor professor);

    List<Turma> findByProfessor(Professor professor);
}