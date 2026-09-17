package com.eurofarma.euroforma.common;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Agrega uma lista de datas reais (ex.: Educando.iniciadoEm) em matrículas por mês, em ordem cronológica. */
public final class EvolucaoMensalCalculator {

    private static final DateTimeFormatter FORMATO_MES = DateTimeFormatter.ofPattern("MMM/yy", new Locale("pt", "BR"));

    private EvolucaoMensalCalculator() {
    }

    public static List<PontoEvolucaoDto> calcular(List<LocalDate> datas) {
        Map<YearMonth, Long> contagemPorMes = datas.stream()
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.groupingBy(YearMonth::from, TreeMap::new, Collectors.counting()));

        return contagemPorMes.entrySet().stream()
                .map(entry -> new PontoEvolucaoDto(formatar(entry.getKey()), entry.getValue()))
                .toList();
    }

    private static String formatar(YearMonth mes) {
        String texto = mes.format(FORMATO_MES);
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
