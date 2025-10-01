// Define o pacote para as classes de serviço.
package com.vibecheck.VibeCheck_Backend.services;

// Importações necessárias.
import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.PraticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio relacionada às práticas.
 * Gerencia a criação, abertura e fechamento de práticas baseadas nos registros emocionais.
 */
@Service
public class PraticaService {

    private final PraticaRepository praticaRepository;

    /**
     * Injeção de dependência via construtor (prática recomendada).
     * 
     * @param praticaRepository Repositório para operações com práticas.
     */
    @Autowired
    public PraticaService(PraticaRepository praticaRepository) {
        this.praticaRepository = praticaRepository;
    }

    /**
     * Vincula um registro emocional a uma prática, abrindo ou fechando conforme o tipo.
     * 
     * @param registro O registro emocional a ser vinculado.
     */
    @Transactional
    public void vincularRegistro(RegistroEmocional registro) {
        if (registro.getTipoSubmissao() == TipoAvaliacao.CHECKIN) {
            abrirPratica(registro);
        } else if (registro.getTipoSubmissao() == TipoAvaliacao.CHECKOUT) {
            fecharPratica(registro);
        }
    }

    /**
     * Abre uma nova prática com um registro de check-in.
     * 
     * @param checkinRegistro O registro de check-in que inicia a prática.
     */
    @Transactional
    public Pratica abrirPratica(RegistroEmocional checkinRegistro) {
        // Verifica se já existe uma prática aberta para este aluno nesta turma
        Optional<Pratica> praticaAberta = praticaRepository
                .findFirstByAlunoAndTurmaAndCheckoutIsNullOrderByInicioDesc(
                        checkinRegistro.getAluno(), 
                        checkinRegistro.getTurma()
                );

        if (praticaAberta.isPresent()) {
            // Se já existe uma prática aberta, fecha ela primeiro (com timestamp atual)
            Pratica praticaExistente = praticaAberta.get();
            // Cria um registro de checkout "virtual" para fechar a prática anterior
            RegistroEmocional checkoutVirtual = new RegistroEmocional();
            checkoutVirtual.setAluno(checkinRegistro.getAluno());
            checkoutVirtual.setTurma(checkinRegistro.getTurma());
            checkoutVirtual.setTimestamp(LocalDateTime.now());
            checkoutVirtual.setEmocao(checkinRegistro.getEmocao()); // Usa a mesma emoção
            checkoutVirtual.setTipoSubmissao(TipoAvaliacao.CHECKOUT);
            
            praticaExistente.fechar(checkoutVirtual);
            praticaRepository.save(praticaExistente);
        }

        // Cria e abre uma nova prática
        Pratica novaPratica = new Pratica();
        novaPratica.abrir(checkinRegistro);
        return praticaRepository.save(novaPratica);
    }

    /**
     * Fecha uma prática aberta com um registro de check-out.
     * 
     * @param checkoutRegistro O registro de check-out que finaliza a prática.
     * @return A prática fechada, ou null se não encontrar prática aberta.
     */
    @Transactional
    public Pratica fecharPratica(RegistroEmocional checkoutRegistro) {
        Optional<Pratica> praticaAberta = praticaRepository
                .findFirstByAlunoAndTurmaAndCheckoutIsNullOrderByInicioDesc(
                        checkoutRegistro.getAluno(), 
                        checkoutRegistro.getTurma()
                );

        if (praticaAberta.isPresent()) {
            Pratica pratica = praticaAberta.get();
            pratica.fechar(checkoutRegistro);
            return praticaRepository.save(pratica);
        }
        
        // Se não encontrar prática aberta, pode criar uma prática "isolada" ou ignorar
        // Por enquanto, vamos ignorar para manter a consistência
        return null;
    }

    /**
     * Busca todas as práticas de um aluno.
     * 
     * @param aluno O aluno para buscar as práticas.
     * @return Lista de práticas do aluno.
     */
    public List<Pratica> buscarPraticasPorAluno(Aluno aluno) {
        return praticaRepository.findByAlunoOrderByInicioDesc(aluno);
    }

    /**
     * Busca todas as práticas de uma turma.
     * 
     * @param turma A turma para buscar as práticas.
     * @return Lista de práticas da turma.
     */
    public List<Pratica> buscarPraticasPorTurma(Turma turma) {
        return praticaRepository.findByTurmaOrderByInicioDesc(turma);
    }

    /**
     * Busca práticas de um aluno em um período específico.
     * 
     * @param aluno O aluno para buscar as práticas.
     * @param inicio Data de início do período.
     * @param fim Data de fim do período.
     * @return Lista de práticas do aluno no período.
     */
    public List<Pratica> buscarPraticasPorAlunoEPeriodo(Aluno aluno, LocalDateTime inicio, LocalDateTime fim) {
        return praticaRepository.findByAlunoAndInicioBetween(aluno, inicio, fim);
    }

    /**
     * Busca práticas de uma turma em um período específico.
     * 
     * @param turma A turma para buscar as práticas.
     * @param inicio Data de início do período.
     * @param fim Data de fim do período.
     * @return Lista de práticas da turma no período.
     */
    public List<Pratica> buscarPraticasPorTurmaEPeriodo(Turma turma, LocalDateTime inicio, LocalDateTime fim) {
        return praticaRepository.findByTurmaAndInicioBetween(turma, inicio, fim);
    }

    /**
     * Busca todas as práticas abertas de uma turma.
     * 
     * @param turma A turma para buscar as práticas abertas.
     * @return Lista de práticas abertas da turma.
     */
    public List<Pratica> buscarPraticasAbertasPorTurma(Turma turma) {
        return praticaRepository.findByTurmaAndCheckoutIsNullOrderByInicioDesc(turma);
    }

    /**
     * Busca todas as práticas abertas de um aluno.
     * 
     * @param aluno O aluno para buscar as práticas abertas.
     * @return Lista de práticas abertas do aluno.
     */
    public List<Pratica> buscarPraticasAbertasPorAluno(Aluno aluno) {
        return praticaRepository.findByAlunoAndCheckoutIsNullOrderByInicioDesc(aluno);
    }

    /**
     * Busca uma prática por ID.
     * 
     * @param id O ID da prática.
     * @return Optional contendo a prática, ou vazio se não encontrada.
     */
    public Optional<Pratica> buscarPorId(Long id) {
        return praticaRepository.findById(id);
    }

    /**
     * Busca práticas de um aluno com informações detalhadas.
     * 
     * @param alunoId ID do aluno.
     * @return Lista de práticas com dados do aluno e turma carregados.
     */
    public List<Pratica> buscarPraticasPorAlunoComDetalhes(Long alunoId) {
        return praticaRepository.findByAlunoWithDetails(alunoId);
    }

    /**
     * Busca práticas de uma turma com informações detalhadas.
     * 
     * @param turmaId ID da turma.
     * @return Lista de práticas com dados do aluno e turma carregados.
     */
    public List<Pratica> buscarPraticasPorTurmaComDetalhes(Long turmaId) {
        return praticaRepository.findByTurmaWithDetails(turmaId);
    }
}

