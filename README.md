<h1 align="center">TODO List — Acelera ZG</h1>

## Descrição

Bem-vindo ao projeto desenvolvido como parte do processo treinamento do **Acelera ZG**. Trata-se de um gerenciador de tarefas (TODO List) via console escrito em Java. 

O sistema foi projetado de forma limpa e modular, com foco em usabilidade no terminal, persistência de dados em arquivos e ordenação inteligente de prioridades.

---

## Funcionalidades

- **Gerenciamento Completo (CRUD)**: Crie, liste, atualize e delete suas tarefas de forma simples.
- **Sistema de Alarmes**: Configure múltiplos alarmes com tempo de antecedência customizável para ser alertado antes do término da tarefa.
- **Persistência em CSV**: Os dados das tarefas e alarmes são salvos automaticamente no arquivo `tarefas.csv` após qualquer modificação e carregados na inicialização.
- **Rebalanceamento Automático**: Ao adicionar ou editar uma tarefa, a lista é automaticamente reordenada para manter as tarefas mais prioritárias no topo.
- **Filtros**: Liste suas tarefas filtrando especificamente por Categoria, Prioridade ou Status.

---

## Sistema de Alarmes

O sistema de alarmes permite acompanhar os prazos das tarefas de forma proativa:
- **Múltiplos Alarmes**: Cada tarefa pode ter um ou mais alarmes configurados com antecedência personalizada em minutos (ex.: 30 min, 1 hora, 1 dia).
- **Execução em Segundo Plano**: Enquanto o aplicativo estiver aberto, os alarmes futuros disparam automaticamente em background, emitindo alerta sonoro e notificação visual no console.
- **Alerta na Inicialização**: Ao abrir o sistema, tarefas que já entraram na janela de antecedência são imediatamente notificadas na tela inicial.

---

## Estrutura do Projeto

```
ACZG-TODO/
├── frontend/
│   ├── css/
│   │   └── style.css          # Estilização e layout com Flexbox
│   ├── js/
│   │   └── app.js             # Lógica do CRUD e manipulação dinâmica do DOM
│   └── index.html             # Interface web principal
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   ├── Main.java              # Ponto de entrada da aplicação Java
│                   ├── model/
│                   │   ├── Alarme.java        # Entidade de Alarme (antecedência e estado)
│                   │   ├── Tarefa.java        # Entidade de Tarefa
│                   │   └── TarefaStatus.java  # Enumeração de Status (TODO, DOING, DONE)
│                   ├── services/
│                   │   ├── AlarmeService.java # Agendamento em background e verificação de alarmes
│                   │   ├── CsvService.java    # Responsável pela persistência (I/O)
│                   │   ├── SomService.java    # Execução de alertas sonoros
│                   │   └── TarefaService.java # Lógica de negócios (adicionar, listar, editar, etc)
│                   └── ui/
│                       └── ConsoleUI.java     # Gerenciamento de interface e leitura de inputs
```

---

## Descrição dos Arquivos Principais

| Arquivo              | Descrição                                                                                                                                      |
|----------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `Main.java`          | Classe principal que orquestra o loop da aplicação, os menus primários e a verificação inicial de alarmes.                                     |
| `Alarme.java`        | Entidade que representa um alarme com minutos de antecedência e cálculo do horário de disparo.                                                 |
| `Tarefa.java`        | Entidade que representa os dados de uma tarefa: Nome, Descrição, Data/Hora de Término, Prioridade, Categoria, Status e Lista de Alarmes.       |
| `TarefaStatus.java`  | Enum que define os estados possíveis da tarefa (`TODO`, `DOING`, `DONE`).                                                                      |
| `AlarmeService.java` | Gerencia o agendamento assíncrono em background dos alarmes e a checagem de tarefas no período de antecedência ao iniciar.                     |
| `CsvService.java`    | Salva e carrega a lista de tarefas e alarmes no arquivo `tarefas.csv`.                                                                         |
| `SomService.java`    | Utilitário para reprodução de efeitos sonoros quando um alarme é disparado.                                                                    |
| `TarefaService.java` | Concentra as regras de negócio: adiciona, atualiza, deleta, filtra e exibe o painel de estatísticas, além de fazer a ordenação por prioridade. |
| `ConsoleUI.java`     | Centraliza as interações com o console: limpeza de tela, leitura formatada e segura de textos, números, datas/horas e *enums*.                 |
| `index.html`         | Interface web com formulário para cadastro/edição, filtro por status e tabela de tarefas.                                                     |
| `style.css`          | Estilização do front-end com Flexbox para estruturação responsiva e componentes limpos.                                                        |
| `app.js`             | Controle dinâmico do front-end: manipulação de eventos, ordenação por prioridade e CRUD em memória.                                             |

---

## Estrutura dos Dados (Tarefa)

| Propriedade              | Tipo            | Descrição                                                   |
|--------------------------|-----------------|-------------------------------------------------------------|
| **Nome**                 | `String`        | Título ou nome principal da atividade                       |
| **Descrição**            | `String`        | Detalhamento sobre o que deve ser feito                     |
| **Data/Hora de Término** | `LocalDateTime` | Prazo limite (formato `dd/MM/yyyy HH:mm` ou `dd/MM/yyyy`)   |
| **Prioridade**           | `int`           | Grau de urgência de 1 a 5 (5 sendo o mais urgente)          |
| **Categoria**            | `String`        | Grupo ou contexto da tarefa (ex: Trabalho, Estudos)         |
| **Status**               | `Enum`          | Estado atual da tarefa (`TODO`, `DOING`, `DONE`)            |
| **Alarmes**              | `List<Alarme>`  | Lista de lembretes configurados com tempo de antecedência   |

---

## Tecnologias

### Backend
- **Java**
- **Gradle** (Gerenciador de dependências e build)

### Frontend
- **HTML5** (Estruturação semântica dos elementos e formulários)
- **CSS3** (Estilização e layout responsivo utilizando Flexbox)
- **JavaScript (ES6+)** (Manipulação do DOM e lógica de CRUD em memória)

---

## Como Executar

### Backend (Console Java)
1. Clone o repositório:
    ```bash
    git clone https://github.com/uGustavoB/ACZG-TODO.git
    ```

2. Navegue até o diretório do projeto:
    ```bash
    cd ACZG-TODO
    ```

3. Compile e construa o projeto usando o Gradle Wrapper:
    - **No Windows:**
      ```bash
      ./gradlew.bat build
      ```
    - **No Linux/Mac:**
      ```bash
      ./gradlew build
      ```

4. Execute a aplicação compilada:
    ```bash
    java -cp build/classes/java/main org.example.Main
    ```

### Frontend (Interface Web)
- Abra diretamente o arquivo [`frontend/index.html`](frontend/index.html) no seu navegador preferido ou utilize a extensão **Live Server** no VS Code / IntelliJ IDEA.

---

## Autores

Este projeto foi desenvolvido por:

- **Gustavo Gabriel** - [GitHub](https://github.com/uGustavoB)
