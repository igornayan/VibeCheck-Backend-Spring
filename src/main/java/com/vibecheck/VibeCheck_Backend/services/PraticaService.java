// Define o pacote para as classes de serviço.
package com.vibecheck.VibeCheck_Backend.services;

// Importações necessárias.
import com.vibecheck.VibeCheck_Backend.dtos.PraticaResumoDTO;
import com.vibecheck.VibeCheck_Backend.models.*;
import com.vibecheck.VibeCheck_Backend.repositories.PraticaRepository;
import com.vibecheck.VibeCheck_Backend.strategies.PraticaListagemContext;
import com.vibecheck.VibeCheck_Backend.strategies.PraticaListagemInterface.TipoEstrategia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PraticaService {

    private final PraticaRepository praticaRepository;
    private final PraticaListagemContext praticaListagemContext;

    @Autowired
    public PraticaService(PraticaRepository praticaRepository, PraticaListagemContext praticaListagemContext) {
        this.praticaRepository = praticaRepository;
        this.praticaListagemContext = praticaListagemContext;
    }

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

    // ========== MÉTODOS DE BUSCA BÁSICOS (USADOS PELO CONTROLLER) ==========

    public Optional<Pratica> buscarPorId(Long id) {
        return praticaRepository.findById(id);
    }

    public List<Pratica> buscarPraticasPorAlunoEPeriodo(Aluno aluno, LocalDateTime inicio, LocalDateTime fim) {
        return praticaRepository.findByAlunoAndInicioBetween(aluno, inicio, fim);
    }

    // ========== MÉTODOS QUE UTILIZAM STRATEGY PATTERN ==========

    public List<PraticaResumoDTO> listarPraticas(TipoEstrategia tipoEstrategia, 
                                                Long turmaId, 
                                                Turma turma, 
                                                Aluno aluno, 
                                                LocalDateTime inicio, 
                                                LocalDateTime fim, 
                                                OAuth2AuthenticationToken authentication) {
        return praticaListagemContext.executarEstrategia(tipoEstrategia, turmaId, turma, aluno, inicio, fim, authentication);
    }

    public List<PraticaResumoDTO> listarTodasPraticas() {
        return listarPraticas(TipoEstrategia.TODAS_PRATICAS, null, null, null, null, null, null);
    }

    public List<PraticaResumoDTO> listarPraticasPorTurma(Long turmaId) {
        return listarPraticas(TipoEstrategia.POR_TURMA, turmaId, null, null, null, null, null);
    }

    public List<PraticaResumoDTO> listarPraticasAbertasPorTurma(Long turmaId, Turma turma) {
        return listarPraticas(TipoEstrategia.ABERTAS_POR_TURMA, turmaId, turma, null, null, null, null);
    }

    public List<PraticaResumoDTO> listarMinhasPraticasAbertas(Aluno aluno, OAuth2AuthenticationToken authentication) {
        return listarPraticas(TipoEstrategia.MINHAS_ABERTAS, null, null, aluno, null, null, authentication);
    }

    public List<PraticaResumoDTO> listarPraticasPorTurmaEPeriodo(Long turmaId, Turma turma, LocalDateTime inicio, LocalDateTime fim) {
        return listarPraticas(TipoEstrategia.POR_TURMA_PERIODO, turmaId, turma, null, inicio, fim, null);
    }
}