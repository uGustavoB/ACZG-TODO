# Plano de Implementação — Suíte de Testes Unitários do CRUD (`TarefaService`)

Este documento descreve as tarefas pequenas, ordenadas e numeradas para a implementação da suíte de testes unitários do CRUD no backend utilizando **Spock Framework (Groovy)**, conforme definido em `docs/spec.md`.

---

## Análise de Bugs e Oportunidades de Melhoria

Durante a análise do código atual de `TarefaService.java`, foram identificados os seguintes pontos que demandam atenção:

1. **Alto Acoplamento de I/O nos Métodos de Mutação:**
   - Os métodos `adicionarTarefa`, `atualizarTarefa` e `deletarTarefa` acoplam a regra de manipulação da lista com `ConsoleUI` (bloqueante via `Scanner`), `CsvService` (disco) e `AlarmeService` (threads e áudio).
   - *Ação necessária:* Extrair métodos puros/sobrecargas testáveis em `TarefaService` (ex.: `adicionarTarefa(List<Tarefa>, Tarefa)`, `atualizarTarefa(...)`, `removerTarefa(...)`) para permitir testes unitários isolados, fazendo com que os métodos de console existentes deleguem para essas rotinas em memória.
2. **Fragilidade com Listas Nulas (`NullPointerException`):**
   - Os métodos de consulta (`filtrarPorCategoria`, `filtrarPorPrioridade`, `filtrarPorStatus` e `contarPorStatus`) já são defensivos contra `tarefas == null`.
   - Em contrapartida, as rotinas de adição, atualização e exclusão disparam `NullPointerException` caso a lista seja nula.
   - *Ação necessária:* Adicionar checagens defensivas semelhantes nos métodos desacoplados de mutação.

---

## Tarefas de Implementação

### Tarefa 1: Configurar ambiente Spock no `build.gradle`
- **Objetivo:** Adicionar o plugin Groovy e as dependências do Spock Framework compatíveis com Groovy 4 e JUnit Platform no arquivo de build.
- **Arquivos alterados:** `build.gradle`.
- **Cenários de teste envolvidos:** Infraestrutura de compilação e execução de especificações Spock em `src/test/groovy/`.
- **Como validar a conclusão:** Executar `./gradlew test` e confirmar execução bem-sucedida das tarefas do Gradle (`:compileTestGroovy`, `:testClasses`, `:test`).

---

### Tarefa 2: Desacoplar operações em memória e tratar defensividade em `TarefaService`
- **Objetivo:** Introduzir sobrecargas ou métodos testáveis em `TarefaService` para operações em memória de Create, Update e Delete com proteção contra valores nulos e índices inválidos, mantendo os métodos de console intactos.
  - `adicionarTarefa(List<Tarefa> tarefas, Tarefa novaTarefa)`: insere a tarefa e ordena decrescentemente por prioridade.
  - `atualizarTarefa(List<Tarefa> tarefas, int indice, Tarefa tarefaAtualizada)`: atualiza o elemento na posição e reordena por prioridade (com validação de índice e lista).
  - `removerTarefa(List<Tarefa> tarefas, int indice)`: remove da lista por índice validando limites.
- **Arquivos alterados:** `src/main/java/org/example/services/TarefaService.java`.
- **Cenários de teste envolvidos:** Base operacional para testes de Create, Update e Delete sem dependência de terminal.
- **Como validar a conclusão:** Executar `./gradlew compileJava` sem erros e verificar que a interface de console (`ConsoleUI`/`Main`) continua funcional e sem quebras de contrato.

---

### Tarefa 3: Implementar testes unitários de Consulta e Filtros (Read)
- **Objetivo:** Criar a especificação Spock cobrindo todos os métodos de leitura, filtros e contadores existentes em `TarefaService`.
- **Arquivos criados:** `src/test/groovy/org/example/services/TarefaServiceSpec.groovy`.
- **Cenários de teste envolvidos:**
  - `filtrarPorCategoria`: correspondência exata, case-insensitive, remoção de espaços em branco (`trim`), categoria não encontrada, categoria `null` e lista `null`.
  - `filtrarPorPrioridade`: correspondência de prioridade (1 a 5), prioridade inexistente e lista `null`.
  - `filtrarPorStatus`: correspondência de status (`TODO`, `DOING`, `DONE`), status inexistente, status `null` e lista `null`.
  - `contarPorStatus`: contagem correta para cada status, retorno 0 para status sem tarefas, argumento `null` e lista `null`.
- **Como validar a conclusão:** Executar `./gradlew test --tests "org.example.services.TarefaServiceSpec"` e verificar que todas as features de consulta passam com sucesso.

---

### Tarefa 4: Implementar testes unitários de Criação (Create)
- **Objetivo:** Adicionar testes na especificação Spock para validação da inserção de tarefas e regra de rebalanceamento automático por prioridade.
- **Arquivos alterados:** `src/test/groovy/org/example/services/TarefaServiceSpec.groovy`.
- **Cenários de teste envolvidos:**
  - Adicionar tarefa com prioridade 5 em lista contendo tarefas de prioridade menor (verificar posicionamento no índice 0).
  - Adicionar tarefa com prioridade 1 em lista contendo tarefas de prioridade maior (verificar posicionamento ao final).
  - Adicionar tarefa em lista inicialmente vazia.
  - Adicionar tarefa com lista `null` ou tarefa `null` (garantir que não ocorram quebras indevidas).
- **Como validar a conclusão:** Executar `./gradlew test` e verificar aprovação dos cenários de Create.

---

### Tarefa 5: Implementar testes unitários de Atualização (Update)
- **Objetivo:** Adicionar testes na especificação Spock para validação da atualização de tarefas existentes e subsequente reordenação por prioridade.
- **Arquivos alterados:** `src/test/groovy/org/example/services/TarefaServiceSpec.groovy`.
- **Cenários de teste envolvidos:**
  - Atualizar tarefa existente alterando seus atributos e elevando sua prioridade, garantindo nova ordenação da lista.
  - Atualizar tarefa mantendo sua prioridade e validando a permanência dos dados alterados.
  - Tentar atualizar sobre lista vazia (garantir retorno seguro sem alterações).
  - Tentar atualizar com índice fora do intervalo ou lista `null` (retorno seguro).
- **Como validar a conclusão:** Executar `./gradlew test` e verificar aprovação dos cenários de Update.

---

### Tarefa 6: Implementar testes unitários de Exclusão (Delete)
- **Objetivo:** Adicionar testes na especificação Spock para validação da remoção de tarefas por índice na lista.
- **Arquivos alterados:** `src/test/groovy/org/example/services/TarefaServiceSpec.groovy`.
- **Cenários de teste envolvidos:**
  - Excluir tarefa em índice válido (confirmar remoção da tarefa correta e decremento do tamanho da lista).
  - Tentar excluir sobre lista vazia (garantir que nada seja removido e nenhuma exceção não tratada ocorra).
  - Tentar excluir com índice negativo ou maior/igual ao tamanho da lista (garantir segurança sem falhas indevidas).
  - Tentar excluir sobre lista `null`.
- **Como validar a conclusão:** Executar `./gradlew test` e verificar aprovação dos cenários de Delete.

---

### Tarefa 7: Validação Integrada da Suíte e Relatório de Cobertura
- **Objetivo:** Executar a suíte completa de testes de CRUD de ponta a ponta, inspecionar o relatório HTML e assegurar conformidade total com `docs/spec.md`.
- **Arquivos gerados/verificados:** `build/reports/tests/test/index.html`.
- **Cenários de teste envolvidos:** Todos os cenários de Create, Read, Update e Delete implementados nas tarefas anteriores.
- **Como validar a conclusão:** Executar `./gradlew clean test` confirmando status `BUILD SUCCESSFUL` com 100% das asserções passando.
