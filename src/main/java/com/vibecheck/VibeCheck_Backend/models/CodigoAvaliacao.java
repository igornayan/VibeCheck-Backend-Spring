// Define o pacote para as classes de modelo (entidades do banco de dados).
package com.vibecheck.VibeCheck_Backend.models;

// Importações do Jakarta Persistence (JPA), Lombok, e tipos de dados.
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

/**
 * @Entity: Marca esta classe como uma entidade JPA, mapeando-a para uma tabela no banco de dados.
 * @Table(name = "codigos_avaliacao"): Especifica o nome da tabela no banco de dados.
 * @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor: Anotações Lombok para código limpo.
 */
@Entity
@Table(name = "codigos_avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodigoAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária auto-incrementada.

    // A coluna 'codigo' deve ser única, não nula e ter um tamanho fixo de 6 caracteres.
    @Column(nullable = false, unique = true, length = 6)
    private String codigo;

    /**
     * @Enumerated(EnumType.STRING): Mapeia um enum Java para uma coluna no banco.
     * 'STRING' armazena o nome do enum (ex: "CHECKIN") em vez de seu número ordinal (0).
     * Esta é a abordagem recomendada por ser mais legível e robusta a mudanças no enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAvaliacao tipo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao; // Quando o código foi criado.

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao; // Quando o código expira.

    @Column(nullable = false)
    private boolean ativo; // Flag para indicar se o código pode ser usado.

    /**
     * @ManyToOne: Define um relacionamento "Muitos para Um". Muitos códigos podem pertencer a um professor.
     * fetch = FetchType.LAZY: Otimização de performance. O professor associado só será carregado do banco
     * quando for explicitamente acessado no código (ex: codigo.getProfessor()). Evita consultas desnecessárias.
     * @JoinColumn: Especifica a coluna de chave estrangeira ('professor_id') nesta tabela.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    /**
     * Define o relacionamento "Muitos para Um" com a entidade Turma, usando a mesma estratégia
     * de carregamento LAZY para otimizar a performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    /**
     * @OneToMany: Define um relacionamento "Um para Muitos". Um código pode ter muitos registros emocionais.
     * mappedBy = "codigoAvaliacaoUsado": Indica que este é o lado "inverso" do relacionamento.
     * A entidade 'RegistroEmocional' é a "dona" do relacionamento, através de seu campo 'codigoAvaliacaoUsado'.
     * Isso evita a criação de uma tabela de junção extra.
     */
    @OneToMany(mappedBy = "codigoAvaliacaoUsado")
    private List<RegistroEmocional> registros;
}