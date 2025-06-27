package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistroEmocionalService {

    private final RegistroEmocionalRepository registroRepository;
    private final CodigoAvaliacaoRepository codigoRepository;
    private final AlunoRepository alunoRepository;

    @Autowired
    public RegistroEmocionalService(
            RegistroEmocionalRepository registroRepository,
            CodigoAvaliacaoRepository codigoRepository,
            AlunoRepository alunoRepository) {
        this.registroRepository = registroRepository;
        this.codigoRepository = codigoRepository;
        this.alunoRepository = alunoRepository;
    }

    public RegistroEmocional registrarEmocao(String googleId, String codigo, int emocao) {
        CodigoAvaliacao codigoAvaliacao = codigoRepository
                .findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado."));

        TipoAvaliacao tipo = codigoAvaliacao.getTipo(); // CHECKIN ou CHECKOUT

        Aluno aluno = alunoRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        RegistroEmocional registro = new RegistroEmocional();
        registro.setAluno(aluno);
        registro.setCodigoAvaliacaoUsado(codigoAvaliacao);
        registro.setEmocao(emocao);
        registro.setTipoSubmissao(tipo); // Define dinamicamente o tipo
        registro.setTimestamp(LocalDateTime.now());
        registro.setTurma(codigoAvaliacao.getTurma());

        return registroRepository.save(registro);
    }


    public boolean verificarCodigoValido(String codigo) {
        return codigoRepository.findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now()).isPresent();
    }

    public List<DashboardRegistroDTO> getDashboardRegistros() {
        List<RegistroEmocional> registros = registroRepository.findAllByOrderByTimestampDesc();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return registros.stream()
                .map(r -> new DashboardRegistroDTO(
                        r.getTimestamp().format(formatter),
                        r.getEmocao(),
                        r.getTipoSubmissao().name(),
                        r.getTurma().getNome()
                ))
                .collect(Collectors.toList());
    }
}
