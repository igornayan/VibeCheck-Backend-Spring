# Padrão Strategy - Listagem de Práticas

## Visão Geral

Este módulo implementa o padrão Strategy para encapsular as diferentes formas de listar práticas no sistema VibeCheck. O padrão Strategy permite que o algoritmo de listagem varie independentemente dos clientes que o utilizam.

## Estrutura

### 1. Interface Strategy
- **`PraticaListagemStrategy`**: Interface que define o contrato para todas as estratégias de listagem
- **`TipoEstrategia`**: Enum que identifica os tipos de estratégias disponíveis

### 2. Estratégias Concretas
- **`TodasPraticasStrategy`**: Lista todas as práticas (para professores)
- **`PorTurmaStrategy`**: Lista práticas de uma turma específica (para professores)
- **`AbertasPorTurmaStrategy`**: Lista práticas abertas de uma turma (para professores)
- **`MinhasAbertasStrategy`**: Lista práticas abertas do aluno autenticado (para alunos)
- **`PorTurmaPeriodoStrategy`**: Lista práticas de uma turma em um período específico (para professores)

### 3. Context
- **`PraticaListagemContext`**: Classe que gerencia e executa as estratégias

## Benefícios

1. **Flexibilidade**: Fácil adição de novas estratégias de listagem
2. **Manutenibilidade**: Cada estratégia é independente e focada em uma responsabilidade
3. **Testabilidade**: Estratégias podem ser testadas individualmente
4. **Reutilização**: Estratégias podem ser reutilizadas em diferentes contextos
5. **Extensibilidade**: Novos tipos de listagem podem ser adicionados sem modificar código existente

## Como Usar

```java
// No controller
List<PraticaResumoDTO> dtos = praticaListagemContext.executarEstrategia(
    TipoEstrategia.POR_TURMA, 
    turmaId, null, null, null, null, null
);
```

## Adicionando Nova Estratégia

1. Implemente a interface `PraticaListagemStrategy`
2. Adicione o novo tipo no enum `TipoEstrategia`
3. Anote a classe com `@Component` para injeção automática
4. A estratégia será automaticamente registrada no contexto

## Exemplo de Nova Estratégia

```java
@Component
public class MinhasPraticasStrategy implements PraticaListagemStrategy {
    
    @Override
    public List<PraticaResumoDTO> executar(Long turmaId, Turma turma, Aluno aluno, 
                                          LocalDateTime inicio, LocalDateTime fim, 
                                          OAuth2AuthenticationToken authentication) {
        // Implementação da estratégia
    }
    
    @Override
    public TipoEstrategia getTipo() {
        return TipoEstrategia.MINHAS_PRATICAS;
    }
}
```

