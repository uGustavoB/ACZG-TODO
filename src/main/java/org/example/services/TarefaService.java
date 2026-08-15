package org.example.services;

import org.example.model.Tarefa;
import org.example.model.TarefaStatus;
import org.example.ui.ConsoleUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TarefaService {
    private static final Scanner scanner = new Scanner(System.in);

    public static void adicionarTarefa(List<Tarefa> tarefas) {
        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Adicionar Nova Tarefa");

        String nome = ConsoleUI.lerTexto("Nome: ");

        String descricao = ConsoleUI.lerTexto("Descrição: ");

        LocalDate dataTermino = ConsoleUI.lerData("Data de Término (dd/mm/aaaa): ");

        int prioridade = ConsoleUI.lerEscolha("Prioridade (1 a 5): ", 1, 5);

        String categoria = ConsoleUI.lerTexto("Categoria: ");

        TarefaStatus status = ConsoleUI.lerEnum("Status", TarefaStatus.class);

        Tarefa novaTarefa = new Tarefa(nome, descricao, dataTermino, prioridade, categoria, status);
        tarefas.add(novaTarefa);

        tarefas.sort(Comparator.comparingInt(Tarefa::getPrioridade).reversed());

        System.out.println("Tarefa adicionada com sucesso!");
    }

    public static void exibirMenuListagem(List<Tarefa> tarefas) {
        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Filtros de Listagem");

        if (tarefas.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhuma tarefa cadastrada.");
            return;
        }

        System.out.println("1 - Todas as tarefas");
        System.out.println("2 - Filtrar por Categoria");
        System.out.println("3 - Filtrar por Prioridade");
        System.out.println("4 - Filtrar por Status");
        
        int opcao = ConsoleUI.lerEscolha("Como deseja listar? ", 1, 4);

        List<Tarefa> tarefasFiltradas = tarefas;

        if (opcao == 2) {
            String categoria = ConsoleUI.lerTexto("Digite a categoria: ");
            tarefasFiltradas = tarefas.stream()
                    .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
                    .collect(Collectors.toList());
        } else if (opcao == 3) {
            int prioridade = ConsoleUI.lerEscolha("Digite a prioridade (1 a 5): ", 1, 5);
            tarefasFiltradas = tarefas.stream()
                    .filter(t -> t.getPrioridade() == prioridade)
                    .collect(Collectors.toList());
        } else if (opcao == 4) {
            TarefaStatus status = ConsoleUI.lerEnum("Escolha o Status", TarefaStatus.class);
            tarefasFiltradas = tarefas.stream()
                    .filter(t -> t.getStatus() == status)
                    .collect(Collectors.toList());
        }

        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Lista de Tarefas");

        if (tarefasFiltradas.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhuma tarefa encontrada com esse filtro.");
            return;
        }

        for (Tarefa t : tarefasFiltradas) {
            System.out.println("- " + t.toString());
        }
    }

    public static void listarTarefas(List<Tarefa> tarefas) {
        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Lista de Tarefas");

        if (tarefas.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhuma tarefa cadastrada.");
            return;
        }

        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + " - " + tarefas.get(i).toString());
        }
    }

    public static void atualizarTarefa(List<Tarefa> tarefas) {
        listarTarefas(tarefas);

        if (tarefas.isEmpty()) {
            return;
        }

        int indice = ConsoleUI.lerEscolha("Escolha o número da tarefa para atualizar: ", 1, tarefas.size()) - 1;
        Tarefa tarefa = tarefas.get(indice);

        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Atualizando Tarefa: " + tarefa.getNome());

        String novoNome = ConsoleUI.lerTexto("Nome [" + tarefa.getNome() + "]: ", true);
        if (!novoNome.trim().isEmpty()) {
            tarefa.setNome(novoNome);
        }

        String novaDescricao = ConsoleUI.lerTexto("Descrição [" + tarefa.getDescricao() + "]: ", true);
        if (!novaDescricao.trim().isEmpty()) {
            tarefa.setDescricao(novaDescricao);
        }

        LocalDate novaData = ConsoleUI.lerData("Data de Término (dd/mm/aaaa) [" + tarefa.getDataTermino().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ", true);
        if (novaData != null) {
            tarefa.setDataTermino(novaData);
        }

        Integer novaPrioridade = ConsoleUI.lerEscolha("Prioridade (1 a 5) [" + tarefa.getPrioridade() + "]: ", 1, 5, true);
        if (novaPrioridade != null) {
            tarefa.setPrioridade(novaPrioridade);
        }

        String novaCategoria = ConsoleUI.lerTexto("Categoria [" + tarefa.getCategoria() + "]: ", true);
        if (!novaCategoria.trim().isEmpty()) {
            tarefa.setCategoria(novaCategoria);
        }

        TarefaStatus novoStatus = ConsoleUI.lerEnum("Status [" + tarefa.getStatus() + "]", TarefaStatus.class, true);
        if (novoStatus != null) {
            tarefa.setStatus(novoStatus);
        }

        tarefas.sort(Comparator.comparingInt(Tarefa::getPrioridade).reversed());

        System.out.println("Tarefa atualizada com sucesso!");
    }

    public static void deletarTarefa(List<Tarefa> tarefas) {
        listarTarefas(tarefas);

        if (tarefas.isEmpty()) {
            return;
        }

        int indice = ConsoleUI.lerEscolha("Escolha o número da tarefa para deletar: ", 1, tarefas.size()) - 1;
        Tarefa tarefaRemovida = tarefas.remove(indice);

        System.out.println("Tarefa '" + tarefaRemovida.getNome() + "' deletada com sucesso!");
    }
}
