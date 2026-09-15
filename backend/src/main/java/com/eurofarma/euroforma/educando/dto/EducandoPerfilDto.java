package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.StatusEducando;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public record EducandoPerfilDto(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        LocalDate nascimento,
        Integer idade,
        String curso,
        Integer progresso,
        BigDecimal media,
        LocalDate iniciadoEm,
        EnderecoDto endereco,
        String linkedin,
        String curriculoArquivoNomeOriginal,
        boolean curriculoDisponivel,
        TesteInglesDto testeIngles,
        List<CursoAnteriorDto> cursosAnteriores,
        List<HistoricoDto> historico,
        List<DisciplinaDto> boletim,
        StatusEducando status,
        Integer frequencia,
        String motivoDesistencia
) {
    public static EducandoPerfilDto de(Educando e) {
        Integer idade = e.getNascimento() != null ? Period.between(e.getNascimento(), LocalDate.now()).getYears() : null;
        return new EducandoPerfilDto(
                e.getId(),
                e.getUsuario().getNome(),
                e.getUsuario().getEmail(),
                e.getTelefone(),
                e.getCpf(),
                e.getNascimento(),
                idade,
                e.getCurso().getNome(),
                e.getProgresso(),
                e.getMedia(),
                e.getIniciadoEm(),
                EnderecoDto.de(e.getEndereco()),
                e.getLinkedin(),
                e.getCurriculoArquivoNomeOriginal(),
                e.getCurriculoArquivoPath() != null,
                TesteInglesDto.de(e.getTesteIngles()),
                e.getCursosAnteriores().stream().map(CursoAnteriorDto::de).toList(),
                e.getHistorico().stream().map(HistoricoDto::de).toList(),
                e.getBoletim().stream().map(DisciplinaDto::de).toList(),
                e.getStatus(),
                e.getFrequencia(),
                e.getMotivoDesistencia());
    }
}
