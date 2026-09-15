package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.StatusEducando;

public record EducandoResumoDto(
        Long id,
        String nome,
        String curso,
        Integer frequencia,
        Integer progresso,
        StatusEducando status
) {
    public static EducandoResumoDto de(Educando educando) {
        return new EducandoResumoDto(
                educando.getId(),
                educando.getUsuario().getNome(),
                educando.getCurso().getNome(),
                educando.getFrequencia(),
                educando.getProgresso(),
                educando.getStatus());
    }
}
