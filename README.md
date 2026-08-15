<h1 align="center">TODO List — Acelera ZG</h1>

## Descrição

Bem-vindo ao projeto desenvolvido como parte do processo treinamento do **Acelera ZG**. Trata-se de um gerenciador de tarefas (TODO List) via console escrito em Java. 

O sistema foi projetado de forma limpa e modular, com foco em usabilidade no terminal, persistência de dados em arquivos e ordenação inteligente de prioridades.

---

## Funcionalidades

- **Gerenciamento Completo (CRUD)**: Crie, liste, atualize e delete suas tarefas de forma simples.
- **Persistência em CSV**: Os dados são salvos automaticamente no arquivo `tarefas.csv` após qualquer modificação e carregados na inicialização, garantindo que nada seja perdido.
- **Rebalanceamento Automático**: Ao adicionar ou editar uma tarefa, a lista é automaticamente reordenada para manter as tarefas mais prioritárias no topo.
- **Filtros**: Liste suas tarefas filtrando especificamente por Categoria, Prioridade ou Status.

---

## Estrutura do Projeto

```
src/
└── main/
    └── java/
        └── org/
            └── example/
                ├── Main.java              # Ponto de entrada da aplicação
                ├── model/
                │   ├── Tarefa.java        # Entidade de Tarefa
                │   └── TarefaStatus.java  # Enumeração de Status (TODO, DOING, DONE)
                ├── services/
                │   ├── CsvManager.java    # Responsável pela persistência (I/O)
                │   └── TarefaService.java # Lógica de negócios (adicionar, listar, editar, etc)
                └── ui/
                    └── ConsoleUI.java     # Gerenciamento de interface e leitura de inputs
```

---

## Descrição dos Arquivos Principais

| Arquivo              | Descrição                                                                                                                                      |
|----------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `Main.java`          | Classe principal que orquestra o loop da aplicação, os menus primários e invoca os serviços.                                                   |
| `Tarefa.java`        | Entidade que representa os dados de uma tarefa: Nome, Descrição, Data de Término, Prioridade, Categoria e Status.                              |
| `TarefaStatus.java`  | Enum que define os estados possíveis da tarefa (`TODO`, `DOING`, `DONE`).                                                                      |
| `TarefaService.java` | Concentra as regras de negócio: adiciona, atualiza, deleta, filtra e exibe o painel de estatísticas, além de fazer a ordenação por prioridade. |
| `CsvManager.java`    | Classe utilitária focada em salvar e carregar a lista de tarefas no arquivo `tarefas.csv`, gerenciando escapes para não quebrar a estrutura.   |
| `ConsoleUI.java`     | Centraliza as interações com o console: limpeza de tela, leitura formatada e segura de textos, números, datas e *enums*.                       |

---

## Estrutura dos Dados (Tarefa)

| Propriedade         | Tipo        | Descrição                                           |
|---------------------|-------------|-----------------------------------------------------|
| **Nome**            | `String`    | Título ou nome principal da atividade               |
| **Descrição**       | `String`    | Detalhamento sobre o que deve ser feito             |
| **Data de Término** | `LocalDate` | Prazo limite (formato dd/mm/aaaa)                   |
| **Prioridade**      | `int`       | Grau de urgência de 1 a 5 (5 sendo o mais urgente)  |
| **Categoria**       | `String`    | Grupo ou contexto da tarefa (ex: Trabalho, Estudos) |
| **Status**          | `Enum`      | Estado atual da tarefa (TODO, DOING, DONE)          |

---

## Tecnologias

- **Java**
- **Gradle** (Gerenciador de dependências e build)

---

## Como Executar

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

> Também é possível abrir o projeto diretamente no IntelliJ IDEA e executar a classe `Main.java`.

---

## Autores

Este projeto foi desenvolvido por:

- **Gustavo Gabriel** - [GitHub](https://github.com/uGustavoB)
