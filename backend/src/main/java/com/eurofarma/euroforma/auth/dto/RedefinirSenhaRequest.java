package com.eurofarma.euroforma.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") String novaSenha
) {
}
