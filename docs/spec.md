# Especificação de Testes de Unidade — CRUD de Tarefas (`TarefaService`)

## 1. Contexto e Objetivo

### Contexto
O **ACZG-TODO** é uma aplicação de gerenciamento de tarefas com backend em Java (executado via console) e interface web. As regras de negócio do backend estão concentradas na classe `org.example.services.TarefaService`.

### Objetivo
Definir a especificação da suíte de testes unitários automatizados para as operações de CRUD (Create, Read, Update, Delete) em `TarefaService`, utilizando o framework **Spock (Groovy)** com **tipagem estática** (sem tipagem dinâmica / sem `def`). Os testes visam garantir o comportamento correto das regras de negócio existentes, mantendo isolamento de dependências externas (console, disco e agendador de alarmes).

---

## 2. Regras de Negócio Existentes

Com base no código atual da aplicação:

1. **Criação de Tarefa (Create):**
   - Uma nova tarefa contém: `nome`, `descricao`, `dataTermino` (`LocalDateTime`), `prioridade` (1 a 5), `categoria`, `status` (`TarefaStatus`) e opcionalmente uma lista de `Alarme`.
   - Ao adicionar uma tarefa a uma lista, a coleção deve ser reordenada de forma decrescente por prioridade (`prioridade 5` no topo, `prioridade 1` na base).

2. **Consulta e Filtros (Read):**
   - **Filtrar por Categoria:**
     - Retorna apenas tarefas cuja categoria corresponda à informada.
     - A comparação é *case-insensitive* e ignora espaços em branco nas extremidades (`trim()`).
     - Se a lista de tarefas for nula ou a categoria buscada for nula, o resultado deve ser uma lista vazia.
   - **Filtrar por Prioridade:**
     - Retorna apenas tarefas com a prioridade numérica exata informada.
     - Se a lista de tarefas for nula, o resultado deve ser uma lista vazia.
   - **Filtrar por Status:**
     - Retorna apenas tarefas cujo `TarefaStatus` corresponda ao status informado (`TODO`, `DOING`, `DONE`).
     - Se a lista for nula ou o status for nulo, o resultado deve ser uma lista vazia.
   - **Contagem por Status:**
     - Retorna a quantidade numérica de tarefas com determinado status.
     - Se a lista for nula ou o status for nulo, retorna 0.

3. **Atualização de Tarefa (Update):**
   - A tarefa existente na lista tem seus atributos alterados.
   - A lista de tarefas é reordenada de forma decrescente por prioridade após a alteração.
   - Se a lista estiver vazia, a operação de atualização é interrompida sem modificações.

4. **Exclusão de Tarefa (Delete):**
   - A tarefa no índice selecionado é removida da lista.
   - Se a lista estiver vazia, a operação de exclusão é interrompida sem falhas ou remoções.

---

## 3. Cenários de Sucesso e Falha

### 3.1 Criação (Create)
- **Sucesso:**
  - Adicionar tarefa com prioridade superior a uma lista existente e verificar se ela é inserida e posicionada no topo.
  - Adicionar tarefa com prioridade inferior e verificar se ela é inserida e posicionada ao final.
  - Adicionar tarefa em lista inicialmente vazia.

### 3.2 Consulta e Filtros (Read)
- **Sucesso:**
  - Filtrar por categoria existente (letras maiúsculas/minúsculas diferentes e espaços laterais).
  - Filtrar por prioridade existente (retornar apenas correspondentes).
  - Filtrar por status existente (`TODO`, `DOING` ou `DONE`).
  - Contar tarefas por status existente.
- **Falha / Casos de Borda Existentes:**
  - Filtrar por categoria não encontrada (retornar lista vazia).
  - Filtrar com categoria `null` ou lista `null` (retornar lista vazia).
  - Filtrar por prioridade sem ocorrências (retornar lista vazia).
  - Filtrar com lista de prioridade `null` (retornar lista vazia).
  - Filtrar por status com status `null` ou lista `null` (retornar lista vazia).
  - Contar status com lista `null` ou status `null` (retornar 0).

### 3.3 Atualização (Update)
- **Sucesso:**
  - Atualizar os campos de uma tarefa existente e reordenar a lista conforme a nova prioridade.
  - Atualizar tarefa mantendo campos inalterados quando não informados novos valores.
- **Falha / Casos de Borda Existentes:**
  - Tentar atualizar sobre lista vazia (nenhuma tarefa modificada).

### 3.4 Exclusão (Delete)
- **Sucesso:**
  - Remover tarefa existente pelo índice, reduzindo o tamanho da lista e removendo o elemento correto.
- **Falha / Casos de Borda Existentes:**
  - Tentar excluir sobre lista vazia (nenhuma tarefa removida).

---

## 4. Critérios de Aceite (Formato Given / When / Then)

### Feature: Criação de Tarefas
```gherkin
Cenário: Adicionar nova tarefa com reordenação de prioridade
  Given uma lista de tarefas contendo uma tarefa com prioridade 2
  And uma nova tarefa instanciada com prioridade 5
  When a nova tarefa é adicionada à lista e a ordenação é executada
  Then a lista deve conter 2 tarefas
  And a tarefa com prioridade 5 deve estar no índice 0
  And a tarefa com prioridade 2 deve estar no índice 1

Cenário: Adicionar tarefa em lista vazia
  Given uma lista de tarefas vazia
  And uma nova tarefa instanciada válida
  When a tarefa é adicionada à lista
  Then a lista deve conter exatamente 1 tarefa
  And a tarefa inserida deve corresponder aos dados informados
```

### Feature: Consulta e Filtros de Tarefas
```gherkin
Cenário: Filtrar tarefas por categoria ignorando case e espaços
  Given uma lista de tarefas contendo categorias "Trabalho", "TRABALHO " e "Estudos"
  When o filtro por categoria é executado com " trabalho"
  Then o resultado deve conter 2 tarefas com categoria "Trabalho" e "TRABALHO "
  And não deve conter tarefas da categoria "Estudos"

Cenário: Filtrar tarefas por categoria nula ou lista nula
  Given uma categoria nula ou lista nula
  When o método de filtragem por categoria é acionado
  Then deve retornar uma lista vazia sem lançar NullPointerException

Cenário: Filtrar tarefas por prioridade
  Given uma lista com 3 tarefas de prioridades 1, 3 e 5
  When o filtro por prioridade é executado para o valor 3
  Then o resultado deve conter apenas 1 tarefa
  And a prioridade da tarefa retornada deve ser 3

Cenário: Filtrar tarefas por prioridade em lista nula
  Given uma lista nula de tarefas
  When o filtro por prioridade é executado
  Then deve retornar uma lista vazia

Cenário: Filtrar tarefas por status
  Given uma lista com tarefas nos status TODO, DOING e DONE
  When o filtro por status é executado para TarefaStatus.DOING
  Then apenas as tarefas com status DOING devem ser retornadas

Cenário: Filtrar tarefas por status nulo ou lista nula
  Given um status nulo ou uma lista de tarefas nula
  When o filtro por status é executado
  Then deve retornar uma lista vazia

Cenário: Contar tarefas por status
  Given uma lista com 2 tarefas TODO, 1 DOING e 0 DONE
  When o método contarPorStatus é chamado para cada status
  Then deve retornar 2 para TODO, 1 para DOING e 0 para DONE

Cenário: Contar tarefas por status com argumentos nulos
  Given uma lista nula ou um status nulo
  When o método contarPorStatus é acionado
  Then o retorno deve ser 0
```

### Feature: Atualização de Tarefas
```gherkin
Cenário: Atualizar campos de uma tarefa e reordenar a lista
  Given uma lista com 2 tarefas: Tarefa A (prioridade 4, índice 0) e Tarefa B (prioridade 2, índice 1)
  When os dados da Tarefa B são atualizados alterando seu nome e elevando sua prioridade para 5
  And a reordenação da lista é executada
  Then a Tarefa B deve passar a ocupar o índice 0 da lista
  And o nome atualizado da Tarefa B deve ser refletido
  And a Tarefa A deve passar para o índice 1

Cenário: Tentar atualizar em lista vazia
  Given uma lista de tarefas vazia
  When a rotina de atualização é executada
  Then nenhuma exceção deve ser lançada e a lista deve permanecer vazia
```

### Feature: Exclusão de Tarefas
```gherkin
Cenário: Excluir tarefa existente da lista
  Given uma lista com 2 tarefas: "Tarefa 1" e "Tarefa 2"
  When a tarefa de índice 0 ("Tarefa 1") é excluída
  Then a lista deve conter apenas 1 tarefa
  And a tarefa restante no índice 0 deve ser a "Tarefa 2"

Cenário: Tentar excluir em lista vazia
  Given uma lista de tarefas vazia
  When a rotina de exclusão é executada
  Then nenhuma exclusão deve ocorrer e a lista deve continuar vazia
```

---

## 5. Impactos em Funcionalidades Existentes

- **Desacoplamento em `TarefaService`:**
  - Os métodos atuais `adicionarTarefa`, `atualizarTarefa` e `deletarTarefa` realizam chamadas diretas a métodos estáticos de `ConsoleUI` (I/O bloqueante via `Scanner`), `CsvService` (acesso a disco) e `AlarmeService` (concorrência e áudio).
  - Para permitir testes unitários das regras de negócio sem acoplamento de terminal:
    - O fluxo de tela/console no `ConsoleUI` / `Main` permanece inalterado.
    - Se for necessário refatorar na etapa de implementação (sujeito a autorização), os passos de manipulação de dados em memória serão isolados em métodos puros/testáveis que recebem os dados necessários e a lista.
- **Configuração de Build (`build.gradle`):**
  - Adição do plugin `groovy` e da dependência `spock-core` (compatível com Spock 2.x e JUnit Platform).

---

## 6. O que Ficará Fora do Escopo

1. **Persistência em Disco (`CsvService`):** testes de escrita e leitura do arquivo `tarefas.csv`.
2. **Sistema de Alarmes (`AlarmeService` e `SomService`):**
   - Agendamento de alarmes via `ScheduledExecutorService`.
   - Execução de alertas sonoros (`sons/alarme.wav`).
   - Verificação de alarmes na inicialização.
3. **Interface de Terminal (`ConsoleUI` e `Main`):** leitura de entradas interativas do usuário via `Scanner` e comandos de limpeza de tela ANSI.
4. **Interface Web / Frontend:** testes de HTML, CSS e JavaScript (`frontend/`).
5. **Novas validações de domínio não existentes no código:** regras que não existam no código atual (ex.: proibição de nome em branco ou datas passadas na entidade `Tarefa`).
