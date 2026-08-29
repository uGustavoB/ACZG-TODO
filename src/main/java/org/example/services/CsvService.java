package org.example.services;

import org.example.model.Alarme;
import org.example.model.Tarefa;
import org.example.model.TarefaStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvService {
    private static final String ARQUIVO_CSV = "tarefas.csv";
    private static final String DELIMITADOR = ";";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void salvarTarefas(List<Tarefa> tarefas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.println("Nome;Descricao;DataTermino;Prioridade;Categoria;Status;Alarmes");
            for (Tarefa tarefa : tarefas) {
                String nome = tarefa.getNome().replace(";", ",");
                String descricao = tarefa.getDescricao().replace(";", ",");
                String data = tarefa.getDataTermino().format(DATE_TIME_FORMATTER);
                String categoria = tarefa.getCategoria().replace(";", ",");
                String alarmes = tarefa.getAlarmes().stream()
                        .map(a -> a.getMinutosAntecedencia() + ":" + a.isAtivo())
                        .collect(Collectors.joining("|"));

                writer.printf("%s;%s;%s;%d;%s;%s;%s%n",
                        nome, descricao, data, tarefa.getPrioridade(), categoria, tarefa.getStatus().name(), alarmes);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo CSV: " + e.getMessage());
        }
    }

    public static List<Tarefa> carregarTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CSV);

        if (!arquivo.exists()) {
            return tarefas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha = reader.readLine(); // Pular o cabeçalho
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] dados = linha.split(DELIMITADOR);
                if (dados.length >= 6) {
                    String nome = dados[0];
                    String descricao = dados[1];
                    LocalDateTime dataTermino;

                    if (dados[2].contains(":")) {
                        dataTermino = LocalDateTime.parse(dados[2], DATE_TIME_FORMATTER);
                    } else {
                        dataTermino = LocalDate.parse(dados[2], DATE_FORMATTER).atTime(23, 59);
                    }

                    int prioridade = Integer.parseInt(dados[3]);
                    String categoria = dados[4];
                    TarefaStatus status = TarefaStatus.valueOf(dados[5]);

                    Tarefa tarefa = new Tarefa(nome, descricao, dataTermino, prioridade, categoria, status);

                    if (dados.length >= 7 && !dados[6].trim().isEmpty()) {
                        String[] itens = dados[6].split("\\|");
                        for (String item : itens) {
                            String[] partes = item.split(":");
                            if (partes.length == 2) {
                                int minutos = Integer.parseInt(partes[0]);
                                boolean ativo = Boolean.parseBoolean(partes[1]);
                                Alarme alarme = new Alarme(minutos);
                                alarme.setAtivo(ativo);
                                tarefa.adicionarAlarme(alarme);
                            }
                        }
                    }

                    tarefas.add(tarefa);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo CSV: " + e.getMessage());
        }

        return tarefas;
    }
}
