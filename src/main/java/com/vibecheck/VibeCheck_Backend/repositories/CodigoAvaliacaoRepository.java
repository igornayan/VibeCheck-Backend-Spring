// Define o pacote para as interfaces de repositório.
package com.vibecheck.VibeCheck_Backend.repositories;

// Importações do modelo e do Spring Data.
import com.vibecheck.VibeCheck_Backend.models.CodigoAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @Repository: Marca a interface como um componente de repositório do Spring.
 * Estende JpaRepository para herdar métodos CRUD para a entidade CodigoAvaliacao.
 */
@Repository
public interface CodigoAvaliacaoRepository extends JpaRepository<CodigoAvaliacao, Long> {

    // Encontra um código de avaliação pelo seu valor (a string de 6 caracteres).
    // Retorna um Optional para tratar o caso de o código não existir.
    Optional<CodigoAvaliacao> findByCodigo(String codigo);

    /**
     * Uma consulta derivada mais complexa que combina múltiplas condições.
     * Spring Data JPA entende o nome do método como:
     * "Encontre por 'codigo' E 'ativo' deve ser 'True' E 'dataExpiracao' deve ser 'After' (depois de) a data fornecida".
     * @param codigo O valor do código a ser buscado.
     * @param agora O momento atual, para comparar com a data de expiração.
     * @return Um Optional contendo o código se ele for válido, ou vazio caso contrário.
     */
    Optional<CodigoAvaliacao> findByCodigoAndAtivoTrueAndDataExpiracaoAfter(String codigo, LocalDateTime agora);

    /**
     * Lista todos os códigos ativos de um professor específico.
     * O Spring Data JPA navega pelo relacionamento: 'Professor_Id' significa "busque pela propriedade 'id'
     * do campo 'professor' da entidade CodigoAvaliacao".
     * @param professorId O ID do professor.
     * @return Uma lista de códigos de avaliação ativos para aquele professor.
     */
    List<CodigoAvaliacao> findByProfessor_IdAndAtivoTrue(Long professorId);

    /**
     * Uma consulta de projeção de existência. Em vez de buscar a entidade inteira,
     * o Spring Data gera uma consulta otimizada (ex: SELECT COUNT(*)...) para
     * simplesmente verificar se um registro com o código fornecido existe.
     * É muito mais eficiente do que buscar e depois verificar se o resultado é nulo.
     * @param codigo O código a ser verificado.
     * @return 'true' se o código já existe no banco, 'false' caso contrário.
     */
    boolean existsByCodigo(String codigo);
}