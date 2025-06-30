// Define o pacote para as classes de serviço.
package com.vibecheck.VibeCheck_Backend.services;

// Importações dos modelos, repositórios e outras utilidades.
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

/**
 * @Service: Marca esta classe como um componente de serviço do Spring.
 * É aqui que a lógica de negócio principal da aplicação reside.
 */
@Service
public class CodigoAvaliacaoService {

    // Injeção dos repositórios necessários para interagir com o banco de dados.
    private final CodigoAvaliacaoRepository codigoRepository;
    private final ProfessorRepository professorRepository;
    private final TurmaRepository turmaRepository;

    /**
     * Injeção de dependências via construtor. É a prática recomendada pelo Spring.
     */
    @Autowired
    public CodigoAvaliacaoService(CodigoAvaliacaoRepository codigoRepository,
                                  ProfessorRepository professorRepository,
                                  TurmaRepository turmaRepository) {
        this.codigoRepository = codigoRepository;
        this.professorRepository = professorRepository;
        this.turmaRepository = turmaRepository;
    }

    // Método público que serve como uma fachada para gerar um código de CHECKIN.
    public CodigoAvaliacao gerarCodigoCheckin(String googleId, String nomeTurma) {
        // Delega a lógica para um método privado, especificando o tipo. Promove reutilização de código.
        return gerarCodigo(googleId, nomeTurma, TipoAvaliacao.CHECKIN);
    }

    // Método público para gerar um código de CHECKOUT.
    public CodigoAvaliacao gerarCodigoCheckout(String googleId, String nomeTurma) {
        return gerarCodigo(googleId, nomeTurma, TipoAvaliacao.CHECKOUT);
    }

    // Método privado que centraliza a lógica de criação de códigos.
    private CodigoAvaliacao gerarCodigo(String googleId, String nomeTurma, TipoAvaliacao tipo) {
        // 1. Busca o professor pelo googleId. Se não encontrar, lança uma exceção (fail-fast).
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // 2. Padrão "Get or Create": Tenta buscar a turma. Se não existir, cria uma nova.
        Turma turma = turmaRepository.findByNomeAndProfessor(nomeTurma, professor)
                .orElseGet(() -> {
                    Turma nova = new Turma();
                    nova.setNome(nomeTurma);
                    nova.setProfessor(professor);
                    return turmaRepository.save(nova); // Salva a nova turma e a retorna.
                });

        // 3. Cria e configura o novo objeto CodigoAvaliacao.
        CodigoAvaliacao codigo = new CodigoAvaliacao();
        codigo.setCodigo(gerarCodigoAleatorio(6)); // Gera uma string aleatória.
        codigo.setAtivo(true);
        codigo.setDataCriacao(LocalDateTime.now());
        codigo.setDataExpiracao(LocalDateTime.now().plusMinutes(30)); // Define a validade do código.
        codigo.setProfessor(professor);
        codigo.setTipo(tipo);
        codigo.setTurma(turma);

        // 4. Salva o código no banco e retorna a entidade persistida.
        return codigoRepository.save(codigo);
    }

    // Helper privado para gerar uma string de código aleatório.
    private String gerarCodigoAleatorio(int tamanho) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < tamanho; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }

    // Busca todas as turmas de um professor e retorna apenas os nomes.
    public List<String> listarNomesPorProfessor(String googleId) {
        Professor professor = professorRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        // Usa a API de Streams do Java para transformar a lista de Turma em uma lista de String (nomes).
        return turmaRepository.findByProfessor(professor).stream()
                .map(Turma::getNome)
                .toList();
    }
}