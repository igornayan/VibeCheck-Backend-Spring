package com.vibecheck.VibeCheck_Backend.commands;

import com.vibecheck.VibeCheck_Backend.models.Pratica;
import com.vibecheck.VibeCheck_Backend.models.RegistroEmocional;
import com.vibecheck.VibeCheck_Backend.services.PraticaService;
import org.springframework.transaction.annotation.Transactional;

public class AbrirPraticaCommand implements PraticaCommandInterface {

    private final PraticaService praticaService;
    private final RegistroEmocional registroCheckin;

    public AbrirPraticaCommand(PraticaService praticaService, RegistroEmocional registroCheckin) {
        this.praticaService = praticaService;
        this.registroCheckin = registroCheckin;
    }

    @Override
    @Transactional // A transação é mantida no nível da execução do comando.
    public Pratica executar() {
        // Delega a chamada ao método real no Receiver.
        return praticaService.abrirPratica(registroCheckin);
    }
}