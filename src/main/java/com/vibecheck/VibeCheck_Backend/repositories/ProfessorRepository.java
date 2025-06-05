package com.vibecheck.VibeCheck_Backend.repositories;

import com.vibecheck.VibeCheck_Backend.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    // Buscar um Professor pelo googleId
    Optional<Professor> findByGoogleId(String googleId);
}