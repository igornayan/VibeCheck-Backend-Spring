package com.vibecheck.VibeCheck_Backend.repositories;

import com.vibecheck.VibeCheck_Backend.models.Aluno;
import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.models.TipoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroEmocionalRepository extends JpaRepository<RegistroEmocional, Long> {

    // Encontra todos os registros de um aluno específico
    List<RegistroEmocional> findByAluno(Aluno aluno);

    // Encontra todos os registros associados a um código de avaliação específico
    List<RegistroEmocional> findByCodigoAvaliacaoUsado(CodigoAvaliacao codigoAvaliacao);

    // Encontra todos os registros de um aluno específico de um determinado tipo (CHECKIN ou CHECKOUT)
    List<RegistroEmocional> findByAlunoAndTipoSubmissao(Aluno aluno, TipoAvaliacao tipoSubmissao);

    // Encontra todos os registros de um tipo específico dentro de um período de tempo
    List<RegistroEmocional> findByTipoSubmissaoAndTimestampBetween(TipoAvaliacao tipoSubmissao, LocalDateTime inicio, LocalDateTime fim);

    List<RegistroEmocional> findAllByOrderByTimestampDesc();
}