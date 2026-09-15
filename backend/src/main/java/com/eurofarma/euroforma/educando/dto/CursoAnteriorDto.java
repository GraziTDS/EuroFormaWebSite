package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.CursoAnterior;

public record CursoAnteriorDto(String nome, Integer ano, String situacao) {
    public static CursoAnteriorDto de(CursoAnterior curso) {
        return new CursoAnteriorDto(curso.getNome(), curso.getAno(), curso.getSituacao());
    }
}
