package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tarefa {
    private String nome;
    private String descricao;
    private LocalDateTime dataTermino;
    private int prioridade;
    private String categoria;
    private TarefaStatus status;
    private List<Alarme> alarmes;

    public Tarefa(String nome, String descricao, LocalDateTime dataTermino, int prioridade, String categoria, TarefaStatus status, List<Alarme> alarmes) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.alarmes = (alarmes != null) ? alarmes : new ArrayList<>();
        this.status = status;
    }

    public Tarefa(String nome, String descricao, LocalDateTime dataTermino, int prioridade, String categoria, TarefaStatus status) {
        this(nome, descricao, dataTermino, prioridade, categoria, status, new ArrayList<>());
    }

    public TarefaStatus getStatus() {
        return status;
    }

    public void setStatus(TarefaStatus status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDateTime dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Alarme> getAlarmes() {
        return alarmes;
    }

    public void setAlarmes(List<Alarme> alarmes) {
        this.alarmes = (alarmes != null) ? alarmes : new ArrayList<>();
    }

    public void adicionarAlarme(Alarme alarme) {
        if (alarme != null) {
            alarmes.add(alarme);
        }
    }

    @Override
    public String toString() {
        String infoAlarmes = "";
        if (alarmes != null && !alarmes.isEmpty()) {
            infoAlarmes = " | Alarmes: [" + alarmes.stream().map(Alarme::toString).collect(Collectors.joining(", ")) + "]";
        }
        return String.format("[%s] %s | Prioridade: %d | Categoria: %s | Prazo: %s%s",
                status, nome, prioridade, categoria, dataTermino.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), infoAlarmes);
    }
}
