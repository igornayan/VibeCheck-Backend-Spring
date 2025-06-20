package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public RegistroEmocional realizarCheckin(String googleId, String codigo, int emocao) {
        CodigoAvaliacao codigoAvaliacao = codigoRepository
                .findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado."));

        if (codigoAvaliacao.getTipo() != TipoAvaliacao.CHECKIN) {
            throw new RuntimeException("Este código não é para CHECKIN.");
        }

        Aluno aluno = alunoRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        RegistroEmocional registro = new RegistroEmocional();
        registro.setAluno(aluno);
        registro.setCodigoAvaliacaoUsado(codigoAvaliacao);
        registro.setEmocao(emocao);
        registro.setTipoSubmissao(TipoAvaliacao.CHECKIN);
        registro.setTimestamp(LocalDateTime.now());

        return registroRepository.save(registro);
    }

    public RegistroEmocional realizarCheckout(String googleId, String codigo, int emocao) {
        CodigoAvaliacao codigoAvaliacao = codigoRepository
                .findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado."));

        if (codigoAvaliacao.getTipo() != TipoAvaliacao.CHECKOUT) {
            throw new RuntimeException("Este código não é para CHECKOUT.");
        }

        Aluno aluno = alunoRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        RegistroEmocional registro = new RegistroEmocional();
        registro.setAluno(aluno);
        registro.setCodigoAvaliacaoUsado(codigoAvaliacao);
        registro.setEmocao(emocao);
        registro.setTipoSubmissao(TipoAvaliacao.CHECKOUT);
        registro.setTimestamp(LocalDateTime.now());

        return registroRepository.save(registro);
    }
}
