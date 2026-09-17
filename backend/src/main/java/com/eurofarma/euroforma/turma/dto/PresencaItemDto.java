package com.eurofarma.euroforma.turma.dto;

public record PresencaItemDto(
        Long educandoId,
        String nome,
        boolean presente
) {
}
