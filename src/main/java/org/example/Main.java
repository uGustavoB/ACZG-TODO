package org.example;

import org.example.model.Tarefa;
import org.example.services.TarefaService;
import org.example.ui.ConsoleUI;

import java.util.List;

public class Main {
    private static List<Tarefa> tarefas;

    public static void main(String[] args) {
        boolean executando = true;
        tarefas = new java.util.ArrayList<>();

        while (executando) {
            ConsoleUI.limparTela();
            ConsoleUI.imprimirCabecalho("ACZG - TODO List");

            ConsoleUI.imprimirMensagem("Bem-vindo ao ACZG - TODO List!");
            ConsoleUI.imprimirMensagem("Crie e gerencie suas tarefas de forma eficiente.");

            int opcao = ConsoleUI.pedirOpcaoPrincipal();
            switch (opcao) {
                case 1:
                    gerenciarTarefas();
                    break;
                case 2:
                    System.out.println("Saindo...");
                    System.exit(0);
            }
        }
    }

    private static void gerenciarTarefas() {
        boolean voltando = false;

        while (!voltando) {
            ConsoleUI.limparTela();
            ConsoleUI.imprimirCabecalho("Gerenciar Tarefas");

            int opcao = ConsoleUI.pedirOpcaoTarefa();

            switch (opcao) {
                case 1:
                    TarefaService.adicionarTarefa(tarefas);
                    ConsoleUI.aguardarContinuacao();
                    break;

                case 2:
                    TarefaService.listarTarefas(tarefas);
                    ConsoleUI.aguardarContinuacao();
                    break;

                case 3:
                    TarefaService.atualizarTarefa(tarefas);
                    ConsoleUI.aguardarContinuacao();
                    break;

                case 4:
                    TarefaService.deletarTarefa(tarefas);
                    ConsoleUI.aguardarContinuacao();
                    break;

                case 5:
                    voltando = true;
                    break;
            }
        }
    }
}