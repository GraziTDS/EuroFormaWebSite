package com.eurofarma.euroforma.assistente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChatMensagemDto(
        @NotBlank @Pattern(regexp = "user|assistant") String role,
        @NotBlank String content
) {
}
