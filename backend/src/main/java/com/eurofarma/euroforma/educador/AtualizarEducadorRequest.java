package com.eurofarma.euroforma.educador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarEducadorRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotNull PapelEducador papel,
        @NotNull @Min(0) Integer turmas
) {
}
