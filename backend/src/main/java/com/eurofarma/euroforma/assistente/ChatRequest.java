package com.eurofarma.euroforma.assistente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChatRequest(
        @NotEmpty @Size(max = 30) List<@Valid ChatMensagemDto> historico
) {
}
