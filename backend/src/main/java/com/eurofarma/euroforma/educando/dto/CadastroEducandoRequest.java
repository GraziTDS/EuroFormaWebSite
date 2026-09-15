package com.eurofarma.euroforma.educando.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroEducandoRequest(
        @NotBlank String nomeCompleto,
        String cpf,
        String telefone,
        @NotBlank @Email String email,
        @NotNull Long cursoId
) {
}
