package com.eurofarma.euroforma.educador;

import com.eurofarma.euroforma.common.PontoEvolucaoDto;
import com.eurofarma.euroforma.educando.StatusEducando;
import com.eurofarma.euroforma.educando.dto.EducandoResumoDto;

import java.util.List;
import java.util.Map;

public record EducadorDashboardDto(
        long totalAlunos,
        long ativos,
        long concluintes,
        int taxaConclusao,
        Map<StatusEducando, Long> distribuicaoPorStatus,
        List<EducandoResumoDto> recentes,
        List<PontoEvolucaoDto> evolucaoMensal
) {
}
