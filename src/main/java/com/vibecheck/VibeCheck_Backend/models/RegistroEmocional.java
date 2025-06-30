// Define o pacote para as classes de modelo.
package com.vibecheck.VibeCheck_Backend.models;

// Importações do Jakarta Persistence (JPA), Lombok, etc.
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Entity: Marca esta classe como uma entidade JPA.
 * @Table(name = "registros_emocionais"): Define o nome da tabela no banco de dados.
 * @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor: Anotações Lombok.
 */
@Entity
@Table(name = "registros_emocionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEmocional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária.

    @Column(nullable = false)
    private Integer emocao; // O valor da emoção registrada pelo aluno.

    @Column(nullable = false)
    private LocalDateTime timestamp; // O momento exato em que o registro foi criado.

    /**
     * @Enumerated(EnumType.STRING): Armazena o tipo de submissão (ex: "CHECKIN") como texto,
     * o que é uma prática recomendada para Enums.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_submissao", nullable = false)
    private TipoAvaliacao tipoSubmissao;

    /**
     * @ManyToOne: Muitos registros podem pertencer a um Aluno.
     * fetch = FetchType.LAZY: Otimização de performance. O objeto Aluno só será
     * carregado do banco quando for explicitamente necessário.
     * @JoinColumn: Define a chave estrangeira 'aluno_id' nesta tabela.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    /**
     * Relacionamento ManyToOne com CodigoAvaliacao. Este é o lado "dono" do relacionamento
     * bidirecional que vimos na entidade CodigoAvaliacao.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_avaliacao_id", nullable = false)
    private CodigoAvaliacao codigoAvaliacaoUsado;

    /**
     * Relacionamento ManyToOne com Turma. Incluir a turma diretamente aqui é uma
     * otimização de performance (veja a análise abaixo).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;
}