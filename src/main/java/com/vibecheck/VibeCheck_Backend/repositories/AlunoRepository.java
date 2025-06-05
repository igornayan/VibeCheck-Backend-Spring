package com.vibecheck.VibeCheck_Backend.repositories;

import com.vibecheck.VibeCheck_Backend.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Buscar um Aluno pelo googleId
    Optional<Aluno> findByGoogleId(String googleId);
}