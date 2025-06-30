// Define o pacote para as classes de serviço.
package com.vibecheck.VibeCheck_Backend.services;

// Importações de modelos, repositórios, DTOs e outras utilidades.
import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import com.vibecheck.VibeCheck_Backend.dtos.DashboardRegistroDTO;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Service: Marca a classe como um componente de serviço do Spring,
 * contendo a lógica de negócio da aplicação.
 */
@Service
public class RegistroEmocionalService {

    // Injeção dos repositórios necessários.
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
     * Orquestra o processo de registrar uma nova emoção.
     * @param googleId O ID do aluno que está fazendo o registro.
     * @param codigo O código de avaliação que está sendo usado.
     * @param emocao O valor da emoção.
     * @return O objeto RegistroEmocional que foi salvo no banco.
     */
    public RegistroEmocional registrarEmocao(String googleId, String codigo, int emocao) {
        // 1. Valida o código: verifica se ele existe, está ativo e não expirou.
        // Se não for válido, lança uma exceção, interrompendo o processo.
        CodigoAvaliacao codigoAvaliacao = codigoRepository
                .findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado."));

        // 2. Extrai o tipo de avaliação (CHECKIN/CHECKOUT) do código validado.
        TipoAvaliacao tipo = codigoAvaliacao.getTipo();

        // 3. Encontra o aluno no banco de dados usando o googleId.
        Aluno aluno = alunoRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        // 4. Cria e popula o novo objeto de registro com todos os dados necessários.
        RegistroEmocional registro = new RegistroEmocional();
        registro.setAluno(aluno);
        registro.setCodigoAvaliacaoUsado(codigoAvaliacao);
        registro.setEmocao(emocao);
        registro.setTipoSubmissao(tipo);
        registro.setTimestamp(LocalDateTime.now());
        // Inclui a turma diretamente para otimizar futuras consultas (denormalização).
        registro.setTurma(codigoAvaliacao.getTurma());

        // 5. Salva o registro no banco de dados e o retorna.
        return registroRepository.save(registro);
    }

    /**
     * Um método simples de validação que retorna um booleano.
     * Ideal para um endpoint de API que apenas verifica se um código é válido.
     */
    public boolean verificarCodigoValido(String codigo) {
        // Reutiliza a mesma consulta complexa e apenas verifica se um resultado foi encontrado.
        return codigoRepository.findByCodigoAndAtivoTrueAndDataExpiracaoAfter(codigo, LocalDateTime.now()).isPresent();
    }

    /**
     * Prepara os dados para serem exibidos no dashboard.
     * @return Uma lista de DTOs formatados para a visualização.
     */
    public List<DashboardRegistroDTO> getDashboardRegistros() {
        // 1. Busca todos os registros, já ordenados pelo mais recente.
        List<RegistroEmocional> registros = registroRepository.findAllByOrderByTimestampDesc();

        // 2. Define um formatador para padronizar a exibição da data e hora.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // 3. Usa a API de Streams do Java para transformar a lista de entidades em uma lista de DTOs.
        return registros.stream()
                .map(r -> new DashboardRegistroDTO( // Para cada registro 'r'...
                        r.getTimestamp().format(formatter), // ...formata a data...
                        r.getEmocao(),                      // ...pega a emoção...
                        r.getTipoSubmissao().name(),        // ...pega o nome do tipo...
                        r.getTurma().getNome()              // ...e pega o nome da turma.
                ))
                .collect(Collectors.toList()); // Coleta tudo em uma nova lista de DTOs.
    }
}