package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.BoletimItem;

import java.math.BigDecimal;

public record DisciplinaDto(
        String nome,
        BigDecimal cp1,
        BigDecimal gs1,
        BigDecimal md1,
        BigDecimal cp2,
        BigDecimal gs2,
        BigDecimal md2,
        Integer faltas,
        Integer presencasTotal,
        Integer presencasRealizadas,
        BigDecimal frequenciaPercentual,
        BigDecimal mediaParcial
) {
    public static DisciplinaDto de(BoletimItem item) {
        return new DisciplinaDto(
                item.getDisciplinaNome(),
                item.getCp1(), item.getGs1(), item.getMd1(),
                item.getCp2(), item.getGs2(), item.getMd2(),
                item.getFaltas(), item.getPresencasTotal(), item.getPresencasRealizadas(),
                item.getFrequenciaPercentual(), item.getMediaParcial());
    }
}
