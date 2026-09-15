package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.StatusEducando;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull StatusEducando status,
        String motivoDesistencia
) {
}
