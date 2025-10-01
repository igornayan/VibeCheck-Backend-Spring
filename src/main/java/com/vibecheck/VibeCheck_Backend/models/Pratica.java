// Define o pacote para as classes de modelo.
package com.vibecheck.VibeCheck_Backend.models;

// Importações do Jakarta Persistence (JPA), Lombok, etc.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Entity: Marca esta classe como uma entidade JPA.
 * @Table(name = "praticas"): Define o nome da tabela no banco de dados.
 * @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor: Anotações Lombok.
 */
@Entity
@Table(name = "praticas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pratica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária.

    /**
     * @ManyToOne: Muitas práticas podem pertencer a um Aluno.
     * fetch = FetchType.LAZY: Otimização de performance.
     * @JoinColumn: Define a chave estrangeira 'aluno_id' nesta tabela.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    /**
     * @ManyToOne: Muitas práticas podem pertencer a uma Turma.
     * fetch = FetchType.LAZY: Otimização de performance.
     * @JoinColumn: Define a chave estrangeira 'turma_id' nesta tabela.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    /**
     * @OneToOne: Uma prática tem exatamente um registro de check-in.
     * @JoinColumn: Define a chave estrangeira 'checkin_registro_id' nesta tabela.
     * unique = true: Garante que cada registro de check-in só pode ser usado uma vez.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_registro_id", unique = true, nullable = false)
    private RegistroEmocional checkin;

    /**
     * @OneToOne: Uma prática pode ter um registro de check-out (opcional enquanto aberta).
     * @JoinColumn: Define a chave estrangeira 'checkout_registro_id' nesta tabela.
     * unique = true: Garante que cada registro de check-out só pode ser usado uma vez.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_registro_id", unique = true)
    private RegistroEmocional checkout;

    @Column(nullable = false)
    private LocalDateTime inicio; // Momento do check-in.

    private LocalDateTime fim; // Momento do check-out (null enquanto aberta).

    @Column(name = "duracao_segundos")
    private Long duracaoSegundos; // Duração calculada em segundos.

    @Column(name = "emocao_inicial")
    private Integer emocaoInicial; // Emoção registrada no check-in.

    @Column(name = "emocao_final")
    private Integer emocaoFinal; // Emoção registrada no check-out.

    /**
     * Método para abrir uma nova prática com um registro de check-in.
     * @param in O registro de check-in que inicia a prática.
     */
    public void abrir(RegistroEmocional in) {
        this.checkin = in;
        this.inicio = in.getTimestamp();
        this.emocaoInicial = in.getEmocao();
        this.aluno = in.getAluno();
        this.turma = in.getTurma();
    }

    /**
     * Método para fechar uma prática com um registro de check-out.
     * @param out O registro de check-out que finaliza a prática.
     */
    public void fechar(RegistroEmocional out) {
        this.checkout = out;
        this.fim = out.getTimestamp();
        this.emocaoFinal = out.getEmocao();
        this.duracaoSegundos = Duration.between(inicio, fim).getSeconds();
    }

    /**
     * Verifica se a prática está aberta (tem check-in mas não tem check-out).
     * @return true se a prática está aberta, false caso contrário.
     */
    public boolean isAberta() {
        return checkout == null;
    }

    /**
     * Retorna a duração formatada em horas:minutos:segundos.
     * @return String formatada da duração ou "Em andamento" se aberta.
     */
    public String getDuracaoFormatada() {
        if (duracaoSegundos == null) {
            return "Em andamento";
        }
        
        long horas = duracaoSegundos / 3600;
        long minutos = (duracaoSegundos % 3600) / 60;
        long segundos = duracaoSegundos % 60;
        
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }
}

