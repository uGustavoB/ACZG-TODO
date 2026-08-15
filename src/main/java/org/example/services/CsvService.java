package org.example.services;

import org.example.model.Tarefa;
import org.example.model.TarefaStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvService {
    private static final String ARQUIVO_CSV = "tarefas.csv";
    private static final String DELIMITADOR = ";";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void salvarTarefas(List<Tarefa> tarefas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.println("Nome;Descricao;DataTermino;Prioridade;Categoria;Status");
            for (Tarefa tarefa : tarefas) {
                String nome = tarefa.getNome().replace(";", ",");
                String descricao = tarefa.getDescricao().replace(";", ",");
                String data = tarefa.getDataTermino().format(FORMATTER);
                String categoria = tarefa.getCategoria().replace(";", ",");
                
                writer.printf("%s;%s;%s;%d;%s;%s%n", 
                        nome, descricao, data, tarefa.getPrioridade(), categoria, tarefa.getStatus().name());
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
                    LocalDate dataTermino = LocalDate.parse(dados[2], FORMATTER);
                    int prioridade = Integer.parseInt(dados[3]);
                    String categoria = dados[4];
                    TarefaStatus status = TarefaStatus.valueOf(dados[5]);
                    
                    tarefas.add(new Tarefa(nome, descricao, dataTermino, prioridade, categoria, status));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo CSV: " + e.getMessage());
        }
        
        return tarefas;
    }
}
