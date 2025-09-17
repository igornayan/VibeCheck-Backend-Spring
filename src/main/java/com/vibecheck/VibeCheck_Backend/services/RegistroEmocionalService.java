package com.vibecheck.VibeCheck_Backend.services;

import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.*;
import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    /**
     * Método que verifica se um código de avaliação é válido (está ativo e não expirado).
     *
     * @param codigo Código de avaliação
     * @return Verdadeiro se o código for válido, falso caso contrário.
     */
    @Cacheable(value = "registroEmocional", key = "'dashboard'")
    public boolean verificarCodigoValido(String codigo) {
        return codigoRepository.findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now()).isPresent();
    }

    /**
     * Método para registrar uma emoção de um aluno, associando o código de avaliação.
     *
     * @param googleId ID do aluno.
     * @param codigo   Código de avaliação.
     * @param emocao   Valor da emoção registrada.
     * @return O objeto `RegistroEmocional` recém-criado.
     */
    @CacheEvict(value = "registroEmocional", key = "'dashboard'") // Limpa o cache da dashboard quando um novo registro for feito
    public RegistroEmocional registrarEmocao(String googleId, String codigo, int emocao) {
        CodigoAvaliacao codigoAvaliacao = codigoRepository
                .findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado."));

        TipoAvaliacao tipo = codigoAvaliacao.getTipo();

        Aluno aluno = alunoRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        RegistroEmocional registro = new RegistroEmocional();
        registro.setAluno(aluno);
        registro.setCodigoAvaliacaoUsado(codigoAvaliacao);
        registro.setEmocao(emocao);
        registro.setTipoSubmissao(tipo);
        registro.setTimestamp(LocalDateTime.now());
        registro.setTurma(codigoAvaliacao.getTurma());

        // Salva o registro no banco de dados
        return registroRepository.save(registro);
    }

    /**
     * Prepara os dados para o dashboard, retornando uma lista de DTOs com as informações
     * formatadas de forma adequada.
     *
     * @return Lista de `DashboardRegistroDTO` com os registros emocionais formatados.
     */
    @Cacheable(value = "registroEmocional", key = "'dashboard'") // Cache da dashboard
    public List<DashboardRegistroDTO> getDashboardRegistros() {
        // Busca todos os registros emocionais, já ordenados pelo mais recente
        List<RegistroEmocional> registros = registroRepository.findAllByOrderByTimestampDesc();

        // Define um formatador para a exibição de data e hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Converte a lista de registros em uma lista de DTOs
        return registros.stream()
                .map(r -> new DashboardRegistroDTO(
                        r.getTimestamp().format(formatter),
                        r.getEmocao(),
                        r.getTipoSubmissao().name(),
                        r.getTurma().getNome()))
                .collect(Collectors.toList()); // Retorna a lista de DTOs
    }
}
