package com.eurofarma.euroforma.relatorio;

import java.util.List;

public record RelatorioResponse(
        TipoRelatorio tipo,
        long noFiltro,
        long ativos,
        long concluidos,
        long desistentes,
        List<ItemContagem> matriculasPorCurso,
        List<ItemFrequencia> frequenciaPorEducando,
        List<MotivoDesistenciaDto> motivosDesistencia
) {
}
