package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.TesteIngles;

import java.time.LocalDate;

public record TesteInglesDto(String nivel, Integer pontuacao, LocalDate data) {
    public static TesteInglesDto de(TesteIngles teste) {
        if (teste == null) {
            return null;
        }
        return new TesteInglesDto(teste.getNivel(), teste.getPontuacao(), teste.getData());
    }
}
