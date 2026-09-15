package com.eurofarma.euroforma.auditoria;

import java.time.Instant;

public record AuditoriaDto(
        CampoAuditoria campo,
        String valorAnterior,
        String valorNovo,
        String autorNome,
        Instant criadoEm
) {
    public static AuditoriaDto de(AuditoriaAlteracao a) {
        return new AuditoriaDto(a.getCampo(), a.getValorAnterior(), a.getValorNovo(), a.getAutor().getNome(), a.getCriadoEm());
    }
}
