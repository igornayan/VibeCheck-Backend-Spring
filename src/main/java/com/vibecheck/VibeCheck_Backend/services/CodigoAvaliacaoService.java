package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.CodigoAvaliacaoRepository;
import com.vibecheck.VibeCheck_Backend.repositories.ProfessorRepository;
import com.vibecheck.VibeCheck_Backend.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CodigoAvaliacaoService {

    private final CodigoAvaliacaoRepository codigoRepository;
    private final ProfessorRepository professorRepository;
    private final TurmaRepository turmaRepository;

    @Autowired
    public CodigoAvaliacaoService(CodigoAvaliacaoRepository codigoRepository,
                                  ProfessorRepository professorRepository,
                                  TurmaRepository turmaRepository) {
        this.codigoRepository = codigoRepository;
        this.professorRepository = professorRepository;
        this.turmaRepository = turmaRepository;
    }

    public CodigoAvaliacao gerarCodigoCheckin(String googleId, String nomeTurma) {
        return gerarCodigo(googleId, nomeTurma, TipoAvaliacao.CHECKIN);
    }

    public CodigoAvaliacao gerarCodigoCheckout(String googleId, String nomeTurma) {
        return gerarCodigo(googleId, nomeTurma, TipoAvaliacao.CHECKOUT);
    }

    private CodigoAvaliacao gerarCodigo(String googleId, String nomeTurma, TipoAvaliacao tipo) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Turma turma = turmaRepository.findByNomeAndProfessor(nomeTurma, professor)
                .orElseGet(() -> {
                    Turma nova = new Turma();
                    nova.setNome(nomeTurma);
                    nova.setProfessor(professor);
                    return turmaRepository.save(nova);
                });

        CodigoAvaliacao codigo = new CodigoAvaliacao();
        codigo.setCodigo(gerarCodigoAleatorio(6));
        codigo.setAtivo(true);
        codigo.setDataCriacao(LocalDateTime.now());
        codigo.setDataExpiracao(LocalDateTime.now().plusMinutes(30));
        codigo.setProfessor(professor);
        codigo.setTipo(tipo);
        codigo.setTurma(turma);

        return codigoRepository.save(codigo);
    }

    private String gerarCodigoAleatorio(int tamanho) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < tamanho; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }

    public List<String> listarNomesPorProfessor(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return turmaRepository.findByProfessor(professor).stream()
                .map(Turma::getNome)
                .toList();
    }

}
