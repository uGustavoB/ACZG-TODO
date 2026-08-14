package org.example.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

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

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    public static LocalDate lerData(String mensagem) {
        while (true) {
            String entrada = lerTexto(mensagem);

            try {
                return LocalDate.parse(entrada, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato " + DATE_PATTERN + ".");
            }
        }
    }

    public static <T extends Enum<T>> T lerEnum(String mensagem, Class<T> enumClass) {
        T[] valores = enumClass.getEnumConstants();

        System.out.println("\n" + mensagem);

        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + " - " + valores[i]);
        }

        int opcao = lerEscolha("Escolha: ", 1, valores.length);

        return valores[opcao - 1];
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
