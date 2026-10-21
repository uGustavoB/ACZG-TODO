# Guia dos Agentes — ACZG-TODO

Este documento serve como referência técnica e operacional sobre o repositório **ACZG-TODO**, detalhando a visão geral, a arquitetura, as convenções e as regras para trabalho de agentes automatizados ou desenvolvedores.

---

## 1. Visão Geral

O projeto é uma aplicação de gerenciamento de tarefas (**TODO List**) desenvolvida no contexto do treinamento **Acelera ZG**. O repositório abriga duas interfaces independentes para gestão de tarefas: uma aplicação de console em Java e uma interface web estática.

### Funcionalidades Existentes

#### Backend / CLI (Java)
- **CRUD Completo de Tarefas:**
  - **Criação:** cadastro com `Nome`, `Descrição`, `Data e Hora de Término` (`dd/MM/yyyy HH:mm` ou `dd/MM/yyyy`), `Prioridade` (1 a 5), `Categoria` e `Status` (`TODO`, `DOING`, `DONE`).
  - **Listagem e Filtros:** listagem geral com resumo quantitativo (total e contadores por status) ou filtrada especificamente por Categoria, por Prioridade ou por Status.
  - **Atualização:** alteração seletiva de campos (mantendo valores prévios quando a entrada for vazia), com reordenação imediata por prioridade e atualização automática dos alarmes associados.
  - **Exclusão:** remoção de tarefas com cancelamento automático dos alarmes vinculados.
- **Sistema de Alarmes:**
  - Configuração de múltiplos alarmes por tarefa, definidos em minutos de antecedência.
  - **Verificação na Inicialização (`verificarAlarmesIniciais`):** identifica tarefas no período de antecedência do prazo, emite alertas sonoros (`sons/alarme.wav`) e visuais no terminal, marcando o alarme como inativo.
  - **Agendamento Assíncrono em Background:** utilização de `ScheduledExecutorService` (threads daemon) para disparar alarmes no instante calculado (`dataTermino.minusMinutes(antecedencia)`).
  - **Cancelamento:** alarmes são desativados/cancelados ao marcar a tarefa como `DONE` ou ao excluí-la.
- **Persistência em CSV (`tarefas.csv`):**
  - Leitura e gravação automática no arquivo `tarefas.csv` com delimitador ponto e vírgula (`;`).
  - Sanitização de ponto e vírgula nos textos inseridos (`replace(";", ",")`).
  - Serialização dos alarmes em formato compacto (`<minutos>:<ativo>|<minutos>:<ativo>`).
- **Rebalanceamento por Prioridade:** reordenação automática decrescente por prioridade (prioridade 5 no topo) ao adicionar ou atualizar tarefas.

#### Frontend Web
- Interface estática de página única (SPA simples) com formulário de cadastro e edição.
- Armazenamento em memória e persistência local via `localStorage` (chave `"tarefas"`).
- Ordenação automática decrescente por prioridade.
- Filtro dinâmico por status (`TODO`, `DOING`, `DONE`).
- Atualização em lote: seleção múltipla por checkboxes para alteração do status de múltiplos itens de uma só vez.

---

## 2. Stack e Estrutura

### Tecnologias e Ferramentas
- **Linguagem Principal (Backend):** Java (versão compatível com Java moderno, com recursos como switch expressions, Streams, manipulação com `java.time`).
- **Build & Dependências:** Gradle com Gradle Wrapper (`gradlew`, Gradle 9.6.0).
- **Testes Unitários:** JUnit 6.0.0 (JUnit Jupiter e JUnit Platform Launcher declarados em `build.gradle`).
- **Áudio:** Java Sound API (`javax.sound.sampled`) para reprodução de `.wav`.
- **Frontend:** HTML5, CSS3 (layout responsivo com Flexbox), JavaScript (ES6+ vanilla, manipulação direta de DOM e `localStorage`).
- **Persistência de Dados:** Arquivo CSV (`tarefas.csv`) no backend e `localStorage` no frontend.

### Estrutura de Diretórios
```
ACZG-TODO/
├── .gitignore
├── build.gradle                          # Configuração de plugins, repositórios e dependências (JUnit)
├── gradlew / gradlew.bat                 # Scripts de execução do Gradle Wrapper
├── gradle/wrapper/                       # Binários e propriedades do Gradle Wrapper
├── README.md                             # Documentação principal do projeto
├── settings.gradle                       # Nome do projeto (ACZG-TODO)
├── tarefas.csv                           # Arquivo local de persistência da CLI (CSV)
├── sons/
│   └── alarme.wav                        # Efeito sonoro dos alarmes
├── frontend/
│   ├── index.html                        # Estrutura HTML da interface web
│   ├── css/
│   │   └── style.css                     # Estilos visuais e regras Flexbox
│   └── js/
│       └── app.js                        # Lógica de interface, CRUD em localStorage e eventos
└── src/
    ├── main/
    │   └── java/
    │       └── org/
    │           └── example/
    │               ├── Main.java         # Loop principal, menus do console e inicialização
    │               ├── model/
    │               │   ├── Tarefa.java       # Entidade de Tarefa
    │               │   ├── TarefaStatus.java # Enumeração de status (TODO, DOING, DONE)
    │               │   └── Alarme.java       # Entidade de Alarme e cálculo de disparo
    │               ├── services/
    │               │   ├── TarefaService.java # Lógica de negócio, CRUD, ordenação e filtros
    │               │   ├── AlarmeService.java # Agendamento, disparo e cancelamento de alarmes
    │               │   ├── CsvService.java    # Leitura e persistência no tarefas.csv
    │               │   └── SomService.java    # Execução de áudio via AudioSystem
    │               └── ui/
    │                   └── ConsoleUI.java     # Utilitários de I/O em console e sanitização de entradas
    └── test/
        └── groovy/
            └── org/example/              # Estrutura preparada para testes de unidade
```

---

## 3. Como Executar e Testar

### Pré-requisitos
- JDK instalado (Java compatível com o runtime configurado).
- Navegador moderno para a interface web.

### Backend (Console Java)

1. **Compilar e construir o projeto:**
   - Linux/macOS:
     ```bash
     ./gradlew build
     ```
   - Windows:
     ```bash
     ./gradlew.bat build
     ```

2. **Executar a aplicação via terminal:**
   ```bash
   java -cp build/classes/java/main org.example.Main
   ```

3. **Executar testes automatizados:**
   - Linux/macOS:
     ```bash
     ./gradlew test
     ```
   - Windows:
     ```bash
     ./gradlew.bat test
     ```
   - O relatório detalhado de testes é gerado em: `build/reports/tests/test/index.html`.

### Frontend (Interface Web)
- Abrir diretamente o arquivo `frontend/index.html` no navegador ou inicializar um servidor estático local (ex.: extensão Live Server ou `python3 -m http.server` a partir da raiz do projeto).

---

## 4. Convenções do Repositório

### Nomenclatura e Idioma
- **Idioma do código:** Todas as variáveis, atributos, nomes de métodos, classes de negócio e mensagens de interface com o usuário estão em **português**.
- **Classes e Enums:** `PascalCase` (ex.: `TarefaService`, `TarefaStatus`, `Alarme`, `ConsoleUI`).
- **Métodos e Variáveis:** `camelCase` (ex.: `carregarTarefas`, `verificarAlarmesIniciais`, `dataTermino`, `indiceEdicao`).
- **Constantes:** `UPPER_SNAKE_CASE` (ex.: `ARQUIVO_CSV`, `DELIMITADOR`, `DATE_TIME_FORMATTER`).
- **IDs e Classes no HTML/CSS:** IDs e variáveis JS em `camelCase` (ex.: `formularioTarefa`, `corpoTabelaTarefas`); classes de estilização estrutural em `kebab-case` (ex.: `conteiner-filtro`, `checkbox-tarefa`).
- **Padrão de Commits:** Mensagens baseadas em *Conventional Commits* (ex.: `feat: ...`, `refactor: ...`, `docs: ...`, `chore: ...`).

### Organização e Estilo de Código
- **Separação de Responsabilidades:**
  - `model`: Apenas estruturas de dados e regras diretamente ligadas ao estado interno do objeto (sem I/O de console).
  - `services`: Centralização das regras de negócio, agendamento de threads, leitura/escrita de arquivos e execução de som. Métodos utilitários e de filtro desacoplados para manter testabilidade.
  - `ui`: Isolamento total de interação com o usuário no terminal (`Scanner`, formatações, tratamentos de erro de digitação e sequências de escape ANSI).
  - `Main`: Apenas orquestração do fluxo de execução e laços de repetição dos menus.
- **Tratamento de Dados e Datas:** Uso estrito de `java.time` (`LocalDateTime`, `LocalDate`, `DateTimeFormatter`).
- **Persistência Segura:** No CSV, uso de `try-with-resources` para fechamento de streams e substituição de caracteres delimitadores nas strings (`replace(";", ",")`).

---

## 5. Restrições

- **Não alterar funcionalidades existentes fora do escopo sem autorização prévia e explícita do usuário.**
- Não inventar dependências, frameworks ou funcionalidades inexistentes no código.
- Manter o padrão de código em português para métodos, variáveis e mensagens do domínio.
- Preservar a compatibilidade do formato de persistência do `tarefas.csv` e do `localStorage`.
- Não alterar a lógica de negócio dos serviços ou entidades sem aprovação prévia.
