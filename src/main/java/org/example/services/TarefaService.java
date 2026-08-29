package org.example.services;

import org.example.model.Alarme;
import org.example.model.Tarefa;
import org.example.model.TarefaStatus;
import org.example.ui.ConsoleUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TarefaService {

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

        boolean querAlarme = ConsoleUI.lerConfirmacao("Deseja configurar alarmes para esta tarefa?");
        while (querAlarme) {
            int minutos = ConsoleUI.lerEscolha("Antecedência em minutos (ex: 30 para 30min, 1440 para 1 dia): ", 1, 43200);
            novaTarefa.adicionarAlarme(new Alarme(minutos));
            querAlarme = ConsoleUI.lerConfirmacao("Deseja adicionar outro alarme para esta mesma tarefa?");
        }

        tarefas.add(novaTarefa);
        AlarmeService.agendarAlarmes(novaTarefa);

        tarefas.sort(Comparator.comparingInt(Tarefa::getPrioridade).reversed());
        CsvService.salvarTarefas(tarefas);

        ConsoleUI.imprimirMensagem("Tarefa adicionada com sucesso!");
    }

    public static void exibirMenuListagem(List<Tarefa> tarefas) {
        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Filtros de Listagem");

        if (tarefas == null || tarefas.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhuma tarefa cadastrada.");
            return;
        }

        int opcao = ConsoleUI.pedirOpcaoFiltro();
        List<Tarefa> tarefasFiltradas = processarFiltro(tarefas, opcao);

        exibirTarefasFiltradas(tarefas, tarefasFiltradas, opcao);
    }

    public static List<Tarefa> filtrarPorCategoria(List<Tarefa> tarefas, String categoria) {
        if (tarefas == null || categoria == null) {
            return List.of();
        }
        String categoriaBuscada = categoria.trim();
        return tarefas.stream()
                .filter(t -> t.getCategoria() != null && t.getCategoria().trim().equalsIgnoreCase(categoriaBuscada))
                .collect(Collectors.toList());
    }

    public static List<Tarefa> filtrarPorPrioridade(List<Tarefa> tarefas, int prioridade) {
        if (tarefas == null) {
            return List.of();
        }
        return tarefas.stream()
                .filter(t -> t.getPrioridade() == prioridade)
                .collect(Collectors.toList());
    }

    public static List<Tarefa> filtrarPorStatus(List<Tarefa> tarefas, TarefaStatus status) {
        if (tarefas == null || status == null) {
            return List.of();
        }
        return tarefas.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    public static long contarPorStatus(List<Tarefa> tarefas, TarefaStatus status) {
        if (tarefas == null || status == null) {
            return 0;
        }
        return tarefas.stream()
                .filter(t -> t.getStatus() == status)
                .count();
    }

    private static List<Tarefa> processarFiltro(List<Tarefa> tarefas, int opcao) {
        return switch (opcao) {
            case 2 -> {
                String categoria = ConsoleUI.lerTexto("Digite a categoria: ");
                yield filtrarPorCategoria(tarefas, categoria);
            }
            case 3 -> {
                int prioridade = ConsoleUI.lerEscolha("Digite a prioridade (1 a 5): ", 1, 5);
                yield filtrarPorPrioridade(tarefas, prioridade);
            }
            case 4 -> {
                TarefaStatus status = ConsoleUI.lerEnum("Escolha o Status", TarefaStatus.class);
                yield filtrarPorStatus(tarefas, status);
            }
            default -> tarefas;
        };
    }

    private static void exibirTarefasFiltradas(List<Tarefa> tarefasOriginais, List<Tarefa> tarefasFiltradas, int opcao) {
        ConsoleUI.limparTela();
        ConsoleUI.imprimirCabecalho("Lista de Tarefas");

        if (tarefasFiltradas.isEmpty()) {
            ConsoleUI.imprimirMensagem("Nenhuma tarefa encontrada com esse filtro.");
            return;
        }

        for (Tarefa t : tarefasFiltradas) {
            System.out.println("- " + t.toString());
        }

        if (opcao == 1) {
            exibirResumoTarefas(tarefasOriginais);
        }
    }

    private static void exibirResumoTarefas(List<Tarefa> tarefas) {
        long concluidas = contarPorStatus(tarefas, TarefaStatus.DONE);
        long aFazer = contarPorStatus(tarefas, TarefaStatus.TODO);
        long emAndamento = contarPorStatus(tarefas, TarefaStatus.DOING);

        System.out.println("\n----------------------------------------");
        System.out.println("Resumo das Tarefas:");
        System.out.println("Total cadastradas: " + tarefas.size());
        System.out.println("Para fazer (TODO): " + aFazer);
        System.out.println("Sendo feitas (DOING): " + emAndamento);
        System.out.println("Concluídas (DONE): " + concluidas);
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

        if (tarefa.getStatus() == TarefaStatus.DONE) {
            AlarmeService.cancelarAlarmes(tarefa);
        } else {
            AlarmeService.cancelarAlarmes(tarefa);
            AlarmeService.agendarAlarmes(tarefa);
        }

        tarefas.sort(Comparator.comparingInt(Tarefa::getPrioridade).reversed());
        CsvService.salvarTarefas(tarefas);

        System.out.println("Tarefa atualizada com sucesso!");
    }

    public static void deletarTarefa(List<Tarefa> tarefas) {
        listarTarefas(tarefas);

        if (tarefas.isEmpty()) {
            return;
        }

        int indice = ConsoleUI.lerEscolha("Escolha o número da tarefa para deletar: ", 1, tarefas.size()) - 1;
        Tarefa tarefaRemovida = tarefas.remove(indice);

        AlarmeService.cancelarAlarmes(tarefaRemovida);
        CsvService.salvarTarefas(tarefas);

        System.out.println("Tarefa '" + tarefaRemovida.getNome() + "' deletada com sucesso!");
    }
}
