package com.vibecheck.VibeCheck_Backend.commands;

import com.vibecheck.VibeCheck_Backend.models.Pratica;
import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.services.PraticaService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Comando Concreto para fechar uma Prática.
 * Armazena a referência ao Receiver (PraticaService) e o payload (RegistroEmocional).
 */
public class FecharPraticaCommand implements PraticaCommandInterface {

    private final PraticaService praticaService;
    private final RegistroEmocional registroCheckout;

    public FecharPraticaCommand(PraticaService praticaService, RegistroEmocional registroCheckout) {
        this.praticaService = praticaService;
        this.registroCheckout = registroCheckout;
    }

    @Override
    @Transactional // A transação é mantida no nível da execução do comando.
    public Pratica executar() {
        // Opcional: Adicionar lógica de auditoria/log/pré-processamento aqui.

        // Delega a chamada ao método real no Receiver.
        return praticaService.fecharPratica(registroCheckout);
    }
}
