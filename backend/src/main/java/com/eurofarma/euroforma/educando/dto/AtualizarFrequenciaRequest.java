package com.eurofarma.euroforma.educando.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AtualizarFrequenciaRequest(
        @NotNull @Min(0) @Max(100) Integer frequencia
) {
}
