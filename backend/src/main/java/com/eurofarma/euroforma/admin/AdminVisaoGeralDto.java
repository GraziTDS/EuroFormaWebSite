package com.eurofarma.euroforma.admin;

import com.eurofarma.euroforma.common.PontoEvolucaoDto;
import com.eurofarma.euroforma.educando.StatusEducando;

import java.util.List;
import java.util.Map;

public record AdminVisaoGeralDto(
        long totalEducandos,
        long ativos,
        int presencaMedia,
        int taxaConclusao,
        long concluidos,
        long educadoresAtivos,
        long educadoresTotal,
        Map<StatusEducando, Long> distribuicaoPorStatus,
        Map<String, Long> educandosPorCurso,
        List<PontoEvolucaoDto> evolucaoMensal
) {
}
