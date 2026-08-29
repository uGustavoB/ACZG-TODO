package org.example.services;

import org.example.model.Alarme;
import org.example.model.Tarefa;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AlarmeService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    private static final Map<Alarme, ScheduledFuture<?>> alarmesAgendados = new ConcurrentHashMap<>();

    public static void agendarAlarmes(Tarefa tarefa) {
        if (tarefa == null || tarefa.getAlarmes() == null) {
            return;
        }
        for (Alarme alarme : tarefa.getAlarmes()) {
            agendarAlarme(tarefa, alarme);
        }
    }

    public static void agendarAlarme(Tarefa tarefa, Alarme alarme) {
        if (alarme == null || tarefa == null || !alarme.isAtivo()) {
            return;
        }

        LocalDateTime disparo = alarme.calcularDataHoraDisparo(tarefa.getDataTermino());
        if (disparo == null) {
            return;
        }

        long atraso = Duration.between(LocalDateTime.now(), disparo).toMillis();
        if (atraso <= 0) {
            return;
        }

        cancelarAlarme(alarme);

        ScheduledFuture<?> future = scheduler.schedule(() -> {
            dispararAlarme(tarefa, alarme);
        }, atraso, TimeUnit.MILLISECONDS);

        alarmesAgendados.put(alarme, future);
    }

    public static void cancelarAlarme(Alarme alarme) {
        if (alarme == null) {
            return;
        }

        ScheduledFuture<?> alarmesFuturos = alarmesAgendados.remove(alarme);
        if (alarmesFuturos != null) {
            alarmesFuturos.cancel(false);
        }
    }

    public static void cancelarAlarmes(Tarefa tarefa) {
        if (tarefa == null || tarefa.getAlarmes() == null) {
            return;
        }
        for (Alarme alarme : tarefa.getAlarmes()) {
            cancelarAlarme(alarme);
        }
    }

    private static void dispararAlarme(Tarefa tarefa, Alarme alarme) {
        alarme.setAtivo(false);
        String dataFormatada = (tarefa.getDataTermino() != null)
                ? tarefa.getDataTermino().format(FORMATTER)
                : "sem data";

        System.out.println("\n\n========================================");
        System.out.println("[ALARME DISPARADO]");
        System.out.println("Tarefa: " + tarefa.getNome());
        System.out.println("Aviso: " + alarme + " do término (" + dataFormatada + ")");
        System.out.println("========================================");
        System.out.print("\n> ");
    }
}
