package com.eurofarma.euroforma.curso;

public record CursoDto(Long id, String nome) {
    public static CursoDto de(Curso curso) {
        return new CursoDto(curso.getId(), curso.getNome());
    }
}
