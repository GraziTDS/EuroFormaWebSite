package com.eurofarma.euroforma.evento;

import java.time.LocalDateTime;

public record EventoDto(
        Long id,
        TipoEvento tipo,
        String titulo,
        String descricao,
        String local,
        LocalDateTime dataHora,
        Integer vagasTotal,
        long vagasOcupadas,
        boolean inscrito
) {
}
