package com.eurofarma.euroforma.turma.dto;

import java.time.LocalDate;

public record AulaDto(
        Long id,
        LocalDate data,
        String tema,
        long totalPresentes,
        long totalEducandos
) {
}
