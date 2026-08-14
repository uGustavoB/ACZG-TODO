package org.example.ui;

import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void imprimirCabecalho(String titulo) {
        System.out.println("\n========================================");
        System.out.println("  " + titulo.toUpperCase());
        System.out.println("========================================");
    }

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void aguardarContinuacao() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
        limparTela();
    }

    public static void imprimirMensagem(String msg) {
        System.out.println(msg);
    }

    public static int lerEscolha(String mensagem, int min, int max) {
        int escolha = -1;
        while (escolha < min || escolha > max) {
            System.out.print("\n" + mensagem);
            try {
                escolha = Integer.parseInt(scanner.nextLine());
                if (escolha < min || escolha > max) {
                    System.out.println("Opção inválida. Escolha entre " + min + " e " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
        limparTela();
        return escolha;
    }

    public static int pedirOpcaoPrincipal() {
        System.out.println("\nEscolha uma ação:");
        System.out.println("1 - Gerenciar tarefas");
        System.out.println("2 - Sair");

        return lerEscolha("Sua escolha: ", 1, 2);
    }

    public static int pedirOpcaoTarefa() {
        System.out.println("\nEscolha uma ação:");
        System.out.println("1 - Criar tarefa");
        System.out.println("2 - Listar tarefas");
        System.out.println("3 - Deletar tarefa");
        System.out.println("4 - Voltar ao menu principal");

        return lerEscolha("Sua escolha: ", 1, 4);
    }
}
