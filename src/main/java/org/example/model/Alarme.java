package org.example.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Alarme {
    private int minutosAntecedencia;
    private boolean ativo;

    public Alarme(int minutosAntecedencia) {
        this.minutosAntecedencia = minutosAntecedencia;
        this.ativo = true;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getMinutosAntecedencia() {
        return minutosAntecedencia;
    }

    public void setMinutosAntecedencia(int minutosAntecedencia) {
        this.minutosAntecedencia = minutosAntecedencia;
    }

    @Override
    public String toString() {
        if (minutosAntecedencia >= 1440 && minutosAntecedencia % 1440 == 0) {
            return (minutosAntecedencia / 1440) + " dia(s) antes";
        } else if (minutosAntecedencia >= 60 && minutosAntecedencia % 60 == 0) {
            return (minutosAntecedencia / 60) + " hora(s) antes";
        }
        return minutosAntecedencia + " min antes";
    }

    public LocalDateTime calcularDataHoraDisparo(LocalDate dataTermino) {
        if (dataTermino == null) {
            return null;
        }
        return dataTermino.atTime(LocalTime.of(23, 59)).minusMinutes(minutosAntecedencia);
    }
}
