package com.eurofarma.euroforma.turma.dto;

public record TurmaDto(
        Long id,
        String nome,
        String curso,
        String educadorNome,
        long totalEducandos
) {
}
