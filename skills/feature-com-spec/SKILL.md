---
name: feature-com-spec
description: >-
  Metodologia orientada a especificação (Spec-Driven Development) para desenvolvimento
  incremental de features, refatorações ou suítes de testes com validação rigorosa,
  revisão de diffs e commits semânticos por tarefa.
---

# Feature com Especificação (Spec-Driven Development)

Este guia estabelece o fluxo de trabalho padronizado e agnóstico de linguagem para concepção, especificação, planejamento, execução incremental e validação de novas funcionalidades, refatorações ou suítes de testes em qualquer base de código.

---

## 1. Visão Geral e Entradas Necessárias

### Objetivo
Garantir previsibilidade, rastreabilidade e qualidade na entrega de software através do alinhamento prévio das regras de negócio e critérios de aceite em documentos de especificação (`spec.md`) e planos atômicos (`plano.md`), antes de qualquer modificação no código-fonte.

### Entradas Necessárias
Para iniciar o fluxo, o desenvolvedor ou agente deve dispor de:
- **Demanda / Requisito do Usuário:** Descrição funcional do problema a ser resolvido, nova feature ou bugfix.
- **Acesso à Base de Código:** Repositório clonado e ambiente de execução configurado (SDKs, runtime, dependências).
- **Mecanismos de Build e Teste:** Scripts de compilação e suíte de testes automatizados do projeto (ex.: Gradle, Maven, npm, pytest, cargo, go test).

---

## 2. Fase 1: Reconhecimento do Repositório e `AGENTS.md`

Antes de planejar ou alterar código, é obrigatório entender o contexto em que a aplicação opera.

### Ações
1. **Mapeamento Arquitetural:**
   - Identificar a estrutura de diretórios, linguagens utilizadas e divisão de camadas (ex.: model, service, controller, ui, infra).
   - Identificar as tecnologias de build, dependências de teste e padrões de formatação.
2. **Criação ou Atualização do `AGENTS.md`:**
   - Caso o projeto não possua um documento de diretrizes operacionais para agentes e desenvolvedores, criar o arquivo `AGENTS.md` na raiz do projeto.
   - O `AGENTS.md` deve registrar:
     - Visão geral da aplicação e funcionalidades existentes.
     - Stack tecnológica e comandos exatos de build, execução e testes.
     - Convenções de nomenclatura, estilo de código e idioma.
     - Restrições arquiteturais e boas práticas (ex.: manter documentação existente, não quebrar compatibilidade de contratos ou formatos de persistência).

---

## 3. Fase 2: Especificação Técnica Antes do Código (`spec.md`)

> [!IMPORTANT]
> **Regra de Ouro:** Nenhuma linha de código de produção ou de teste deve ser escrita ou modificada antes que a especificação esteja completamente definida, revisada e aprovada pelo usuário.

### Estrutura Obrigatória da Especificação
A especificação deve ser criada em `docs/spec.md` (ou pasta de documentação correspondente) e conter:

1. **Contexto e Objetivo:**
   - Explicação concisa da motivação e do escopo da entrega.
2. **Regras de Negócio Existentes e Novas:**
   - Definição detalhada dos comportamentos esperados, invariantes de domínio, validações e restrições técnicas.
   - Explicitação de convenções técnicas (ex.: tipagem estática vs. dinâmica, contratos de API, formatos de dados).
3. **Cenários de Sucesso, Falha e Casos de Borda:**
   - Lista exaustiva de cenários esperados, incluindo dados nulos, coleções vazias, valores limites e erros de entrada.
4. **Critérios de Aceite em Formato Given / When / Then (Gherkin):**
   - Especificação comportamental legível e testável para cada funcionalidade.
5. **Impactos em Funcionalidades Existentes:**
   - Avaliação de acoplamentos, necessidade de refatorações prévias ou extensões de contratos.
6. **O que Ficará Fora do Escopo:**
   - Delimitação estrita do que NÃO será tratado nesta entrega, prevenindo *scope creep*.

---

## 4. Fase 3: Aprovação e Versionamento da Spec

1. **Revisão com o Usuário:** Apresentar a especificação ao usuário para validação das regras de negócio, premissas e casos de borda.
2. **Ajustes:** Incorporar feedbacks antes de avançar.
3. **Commit da Spec:** Realizar commit semântico contendo exclusivamente a documentação da especificação:
   ```bash
   git add docs/spec.md
   git commit -m "docs: define especificacao para <nome da feature>"
   ```

---

## 5. Fase 4: Plano de Implementação Incremental (`plano.md`)

Com a especificação aprovada, decompor o trabalho em tarefas pequenas, sequenciais e interdependentes no arquivo `docs/plano.md`.

### Diretrizes do Plano
- **Ordem Lógica:**
  1. Configuração de infraestrutura/dependências de build (se necessário).
  2. Refatorações de desacoplamento ou preparação de arquitetura sem quebra de comportamento.
  3. Implementação incremental por componente ou operação (ex.: no CRUD: Read -> Create -> Update -> Delete).
  4. Validação integrada e documentação final.
- **Detalhamento por Tarefa:**
  - **Objetivo claro:** O que exatamente será feito.
  - **Arquivos criados ou alterados:** Lista restrita e mínima de arquivos afetados.
  - **Cenários de teste envolvidos:** Critérios da `spec.md` que serão cobertos.
  - **Como validar a conclusão:** Comando exato para testar localmente antes de prosseguir.
- **Versionamento:** Commitar o plano:
  ```bash
  git add docs/plano.md
  git commit -m "docs: define plano de implementacao para <nome da feature>"
  ```

---

## 6. Fase 5: Implementação Incremental (Uma Tarefa por Vez)

> [!CAUTION]
> **Nunca execute múltiplas tarefas simultaneamente.** O avanço deve ser estritamente sequencial: implementar -> validar -> revisar -> commitar -> próxima tarefa.

### Regras de Execução
1. **Isolamento de Escopo:**
   - Modifique apenas os arquivos estritamente necessários para a tarefa em curso.
   - Não adiante testes ou funcionalidades da tarefa seguinte.
   - Em testes unitários, isole ações: ao testar uma operação específica (ex.: Consulta), não acople nem execute métodos de mutação (Criação, Edição, Deleção) que pertençam a outras tarefas.
2. **Aderência aos Padrões do Projeto:**
   - Seguir rigorosamente as convenções de tipagem e idioma declaradas em `AGENTS.md` e `docs/spec.md`.
   - Se a especificação exigir tipagem estática, evitar tipagem dinâmica ou variáveis sem tipo explícito.

---

## 7. Fase 6: Revisão do Diff, Execução dos Testes e Commit por Tarefa

Ao concluir a implementação de uma tarefa:

1. **Executar Testes Automatizados:**
   - Rodar o comando específico da tarefa e a suíte geral para certificar-se de que não há quebras:
     ```bash
     # Exemplo genérico
     ./gradlew test --tests "Pacote.ClasseSpec"
     # ou npm test / pytest / go test
     ```
2. **Inspecionar o Diff Local:**
   - Conferir `git status` e `git diff` linha a linha.
   - Garantir que:
     - Não há alterações indesejadas de formatação ou arquivos temporários.
     - Não foram alteradas regras de negócio fora do escopo da tarefa.
3. **Apresentar Resultados ao Usuário:**
   - Informar o resumo das alterações, os cenários executados e eventuais decisões técnicas.
   - Propor uma mensagem de commit semântico (em português).
   - **Aguardar a aprovação do usuário antes de commitar ou avançar.**
4. **Commit Semântico da Tarefa:**
   ```bash
   git add <arquivos-da-tarefa>
   git commit -m "<tipo>: <descricao sucinta em portugues>"
   ```

---

## 8. Fase 7: Validação dos Critérios de Aceite e Regressões

Após a conclusão de todas as tarefas planejadas:

1. **Execução Completa da Suíte do Projeto:**
   - Executar build limpo (`clean build` / `clean test` / equivalente na stack).
   - Confirmar 100% de sucesso sem testes pulados ou ignorados sem justificativa.
2. **Matriz de Rastreabilidade:**
   - Confrontar item a item os critérios de aceite de `docs/spec.md` contra os testes automatizados implementados.
3. **Verificação de Regressão:**
   - Garantir que fluxos pré-existentes da aplicação (CLI, web, APIs, serviços) continuam plenamente operacionais.

---

## 9. Fase 8: Preparação da Descrição do Pull Request

Quando todas as tarefas estiverem commitadas e validadas, estruturar a descrição do Pull Request para revisão da equipe:

```markdown
## Descrição da Mudança
Breve resumo do que foi implementado e qual problema foi resolvido.

## Especificação e Plano
- Referência: `docs/spec.md`
- Plano executado: `docs/plano.md`

## Tarefas Entregues
- [x] Tarefa 1: Configuração do ambiente e dependências
- [x] Tarefa 2: Refatoração defensiva e desacoplamento
- [x] Tarefa 3: Implementação de testes de consulta
- ...

## Como Testar
Comandos exatos para compilar e rodar os testes localmente:
\`\`\`bash
<comando de execucao de testes>
\`\`\`

## Checklist de Qualidade
- [x] Todos os cenários da especificação cobertos.
- [x] Critérios de Given / When / Then seguidos.
- [x] Convenções de estilo e tipagem de `AGENTS.md` respeitadas.
- [x] Sem regressões em funcionalidades existentes.
```

---

## 10. Apêndice A: Modelo de `docs/spec.md`

Utilize este modelo como ponto de partida para qualquer nova especificação:

````markdown
# Especificação — <Nome da Funcionalidade / Componente>

## 1. Contexto e Objetivo
- **Contexto:** <Descrição sucinta do estado atual do sistema.>
- **Objetivo:** <O que esta entrega visa alcançar e quais restrições devem ser observadas.>

---

## 2. Regras de Negócio e Requisitos Técnicos
1. **<Regra 1>:** <Detalhamento da regra, formato de dados, tratamento de tipos e valores padrão.>
2. **<Regra 2>:** <Comportamento esperado para operações de mutação ou consulta.>
3. **<Diretriz de Código>:** <Ex.: Tipagem estática estrita, sem dependências de I/O em métodos de domínio.>

---

## 3. Cenários de Sucesso, Falha e Casos de Borda
- **Cenários de Sucesso:**
  - <Cenário 1: operação com dados nominais.>
  - <Cenário 2: operação com dados válidos em diferentes estados.>
- **Casos de Falha e Borda:**
  - <Cenário 3: entradas nulas ou vazias.>
  - <Cenário 4: limites superiores e inferiores de índices ou valores numéricos.>

---

## 4. Critérios de Aceite (Given / When / Then)

### Feature: <Nome da Feature>
```gherkin
Cenário: <Título do Cenário Nominal>
  Given <contexto inicial ou estado dos dados>
  When <ação ou operação executada>
  Then <resultado esperado mensurável>
  And <estado final das entidades ou retornos>

Cenário: <Título do Cenário de Borda/Falha>
  Given <entrada inválida ou coleção nula>
  When <método é acionado>
  Then <comportamento seguro esperado sem falha descontrolada>
```

---

## 5. Impactos em Funcionalidades Existentes
- <Mapeamento de interfaces públicas afetadas ou refatorações prévias necessárias.>

---

## 6. O que Ficará Fora do Escopo
1. <Item 1 explicitamente excluído desta entrega.>
2. <Item 2 explicitamente excluído desta entrega.>
````

---

## 11. Apêndice B: Modelo de `docs/plano.md`

Utilize este modelo como ponto de partida para o plano de implementação:

```markdown
# Plano de Implementação — <Nome da Feature>

Este plano organiza as tarefas sequenciais e atômicas para implementação dos critérios de `docs/spec.md`.

---

## Tarefa 1: <Configuração de Ambiente / Dependências>
- **Objetivo:** <Configurar dependências, build ou infraestrutura inicial.>
- **Arquivos alterados:** `<caminho/do/arquivo>`
- **Cenários envolvidos:** Infraestrutura base.
- **Como validar:** `<comando de build/teste>`

---

## Tarefa 2: <Refatoração / Desacoplamento Prévio>
- **Objetivo:** <Isolar métodos puros ou desacoplar I/O para viabilizar testes/extensão.>
- **Arquivos alterados:** `<caminho/do/arquivo>`
- **Cenários envolvidos:** Não regressão do comportamento existente.
- **Como validar:** `<comando de validação>`

---

## Tarefa 3: <Implementação Incremental - Parte 1>
- **Objetivo:** <Implementar e testar primeira parte funcional (ex.: Leitura).>
- **Arquivos criados/alterados:** `<caminho/do/arquivo>`
- **Cenários envolvidos:** <Lista de cenários da spec cobertos nesta etapa.>
- **Como validar:** `<comando de teste específico>`

---

## Tarefa N: <Validação Integrada e Documentação>
- **Objetivo:** <Executar suíte completa ponta a ponta e atualizar documentação.>
- **Arquivos gerados/verificados:** `<caminho/dos/relatórios>`
- **Como validar:** `<comando de build e teste limpo>`
```

---

## 12. Apêndice C: Regras e Diretrizes Anti-Deriva (Guardrails)

Para evitar desvios no fluxo de trabalho com agentes ou desenvolvedores:

1. **Nunca assuma requisitos não declarados:** Em caso de ambiguidade sobre regras de negócio, consulte o usuário antes de codificar.
2. **A spec precede o código em 100% dos casos:** Se durante a implementação for identificado que uma regra precisa mudar, **atualize primeiro a `spec.md`**, obtenha aprovação e só então ajuste o código.
3. **Não misture refatorações cosméticas com tarefas funcionais:** Preservar comentários, docstrings e estruturas não relacionadas ao escopo.
4. **Sem commits automáticos desavisados:** Todo commit deve ser validado após revisão do diff e aprovação explícita do usuário.
