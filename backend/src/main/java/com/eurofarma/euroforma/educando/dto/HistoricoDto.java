package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.HistoricoEducando;

import java.time.LocalDate;

public record HistoricoDto(String titulo, LocalDate data) {
    public static HistoricoDto de(HistoricoEducando historico) {
        return new HistoricoDto(historico.getTitulo(), historico.getData());
    }
}
