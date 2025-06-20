package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import com.vibecheck.VibeCheck_Backend.models.Professor;
import com.vibecheck.VibeCheck_Backend.models.TipoAvaliacao;
import com.vibecheck.VibeCheck_Backend.repositories.CodigoAvaliacaoRepository;
import com.vibecheck.VibeCheck_Backend.repositories.ProfessorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
public class CodigoAvaliacaoService {

    private final CodigoAvaliacaoRepository codigoRepository;
    private final ProfessorRepository professorRepository;

    @Autowired
    public CodigoAvaliacaoService(CodigoAvaliacaoRepository codigoRepository,
                                  ProfessorRepository professorRepository) {
        this.codigoRepository = codigoRepository;
        this.professorRepository = professorRepository;
    }

    public CodigoAvaliacao gerarCodigoCheckout(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        CodigoAvaliacao codigo = new CodigoAvaliacao();
        codigo.setCodigo(gerarCodigoAleatorio(6));
        codigo.setAtivo(true);
        codigo.setDataCriacao(LocalDateTime.now());
        codigo.setDataExpiracao(LocalDateTime.now().plusMinutes(30));
        codigo.setProfessor(professor);
        codigo.setTipo(TipoAvaliacao.CHECKOUT); // 👉 Aqui muda para CHECKOUT

        return codigoRepository.save(codigo);
    }


    public CodigoAvaliacao gerarCodigoCheckin(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        CodigoAvaliacao codigo = new CodigoAvaliacao();
        codigo.setCodigo(gerarCodigoAleatorio(6));
        codigo.setAtivo(true);
        codigo.setDataCriacao(LocalDateTime.now());
        codigo.setDataExpiracao(LocalDateTime.now().plusMinutes(30));
        codigo.setProfessor(professor);
        codigo.setTipo(TipoAvaliacao.CHECKIN);

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
}
