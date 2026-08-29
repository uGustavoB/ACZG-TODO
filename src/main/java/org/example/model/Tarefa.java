package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Tarefa {
    private String nome;
    private String descricao;
    private LocalDate dataTermino;
    private int prioridade;
    private String categoria;
    private TarefaStatus status;
    private List<Alarme> alarmes;

    public Tarefa(String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria, TarefaStatus status, List<Alarme> alarmes) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.alarmes = alarmes;
        this.status = status;
    }

    public Tarefa(String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria, TarefaStatus status) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
        this.alarmes = new ArrayList<>();
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

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
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
        this.alarmes = alarmes;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Prioridade: %d | Categoria: %s | Prazo: %s",
                status, nome, prioridade, categoria, dataTermino.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public void adicionarAlarme(Alarme alarme) {
        if (alarme != null) {
            alarmes.add(alarme);
        }
    }
}
