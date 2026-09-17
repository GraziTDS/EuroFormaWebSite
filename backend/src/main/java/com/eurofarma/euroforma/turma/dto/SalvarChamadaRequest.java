package com.eurofarma.euroforma.turma.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SalvarChamadaRequest(
        @NotEmpty List<ItemRequest> presencas
) {
    public record ItemRequest(Long educandoId, boolean presente) {
    }
}
