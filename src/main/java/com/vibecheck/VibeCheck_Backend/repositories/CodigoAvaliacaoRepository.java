package com.vibecheck.VibeCheck_Backend.repositories;

import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoAvaliacaoRepository extends JpaRepository<CodigoAvaliacao, Long> {

    // Encontra um código de avaliação pelo seu valor (string de 6 caracteres)
    Optional<CodigoAvaliacao> findByCodigo(String codigo);

    // Encontra um código de avaliação pelo seu valor, que esteja ativo e dentro da data de expiração
    Optional<CodigoAvaliacao> findByCodigoAndAtivoTrueAndDataExpiracaoAfter(String codigo, LocalDateTime agora);

    // Lista todos os códigos ativos de um determinado professor
    // Para buscar por 'professorId', e como 'CodigoAvaliacao' tem um campo 'Professor professor',
    // o Spring Data JPA pode construir a consulta usando a propriedade 'id' do objeto 'professor'.
    List<CodigoAvaliacao> findByProfessor_IdAndAtivoTrue(Long professorId);
    // Alternativamente, se você passasse o objeto Professor inteiro:
    // List<CodigoAvaliacao> findByProfessorAndAtivoTrue(Professor professor);

    boolean existsByCodigo(String codigo);
}