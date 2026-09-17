package com.eurofarma.euroforma.turma.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CriarAulaRequest(
        @NotNull LocalDate data,
        String tema
) {
}
