// Define o pacote para as classes de modelo.
package com.vibecheck.VibeCheck_Backend.models;

// Importações do Jakarta Persistence (JPA), Lombok, etc.
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @Entity: Marca esta classe como uma entidade JPA.
 * @Table(...): Configura a tabela 'turmas'. A anotação @UniqueConstraint aqui é
 * particularmente interessante.
 * @UniqueConstraint(columnNames = {"nome", "professor_id"}): Cria uma restrição de chave única
 * composta. Isso garante que a COMBINAÇÃO de nome da turma e ID do professor seja única.
 * Ou seja, um mesmo professor não pode ter duas turmas com o mesmo nome.
 */
@Entity
@Table(
        name = "turmas",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nome", "professor_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    /**
     * @ManyToOne: Muitas turmas podem pertencer a um Professor.
     * fetch = FetchType.LAZY: Otimização de performance para carregar o professor apenas quando necessário.
     * @JoinColumn: Define a chave estrangeira 'professor_id' nesta tabela.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    /**
     * @OneToMany: Uma turma pode ter muitos códigos de avaliação.
     * mappedBy = "turma": Indica que o relacionamento é gerenciado pelo campo 'turma' na entidade CodigoAvaliacao.
     * cascade = CascadeType.ALL: Propaga todas as operações de persistência (salvar, atualizar, excluir)
     * da Turma para os seus CodigoAvaliacao associados. Se você excluir uma turma, todos os seus códigos
     * serão excluídos automaticamente.
     * orphanRemoval = true: Se um CodigoAvaliacao for removido da lista 'codigos' de uma turma,
     * ele será excluído do banco de dados. Útil para limpar "órfãos".
     */
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodigoAvaliacao> codigos;
}