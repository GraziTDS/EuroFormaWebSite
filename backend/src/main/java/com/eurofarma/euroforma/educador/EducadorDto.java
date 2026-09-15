package com.eurofarma.euroforma.educador;

import java.time.Instant;

public record EducadorDto(
        Long id,
        String nome,
        String email,
        PapelEducador papel,
        Integer turmas,
        Instant ultimoAcesso,
        boolean ativo
) {
    public static EducadorDto de(Educador educador) {
        return new EducadorDto(
                educador.getId(),
                educador.getUsuario().getNome(),
                educador.getUsuario().getEmail(),
                educador.getPapel(),
                educador.getTurmas(),
                educador.getUltimoAcesso(),
                educador.getUsuario().isAtivo());
    }
}
