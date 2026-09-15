package com.eurofarma.euroforma.relatorio;

import com.eurofarma.euroforma.curso.Curso;
import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.EducandoRepository;
import com.eurofarma.euroforma.educando.StatusEducando;
import com.eurofarma.euroforma.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private EducandoRepository educandoRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @BeforeEach
    void setUp() {
        Curso farmacia = Curso.builder().id(1L).nome("Auxiliar de Farmácia").build();
        Curso logistica = Curso.builder().id(2L).nome("Logística Farmacêutica").build();

        List<Educando> educandos = List.of(
                educando("Ana Carolina Silva", farmacia, StatusEducando.EM_CURSO, 92, null),
                educando("Bruno Henrique Costa", logistica, StatusEducando.CONCLUIDO, 98, null),
                educando("Diego Almeida Santos", farmacia, StatusEducando.EM_CURSO, 78, null),
                educando("Gabriela Pinheiro Rocha", farmacia, StatusEducando.DESISTENTE, 32,
                        "Conflito de horário com novo emprego."));

        when(educandoRepository.findAll()).thenReturn(educandos);
    }

    private Educando educando(String nome, Curso curso, StatusEducando status, int frequencia, String motivo) {
        Usuario usuario = Usuario.builder().nome(nome).build();
        return Educando.builder()
                .usuario(usuario)
                .curso(curso)
                .status(status)
                .frequencia(frequencia)
                .motivoDesistencia(motivo)
                .build();
    }

    @Test
    void gerarMatriculasPorCursoDeveAgruparCorretamente() {
        RelatorioResponse resposta = relatorioService.gerar(TipoRelatorio.MATRICULAS_POR_CURSO);

        assertThat(resposta.noFiltro()).isEqualTo(4);
        assertThat(resposta.ativos()).isEqualTo(2);
        assertThat(resposta.concluidos()).isEqualTo(1);
        assertThat(resposta.desistentes()).isEqualTo(1);
        assertThat(resposta.matriculasPorCurso())
                .extracting(ItemContagem::chave, ItemContagem::total)
                .containsExactlyInAnyOrder(
                        org.assertj.core.groups.Tuple.tuple("Auxiliar de Farmácia", 3L),
                        org.assertj.core.groups.Tuple.tuple("Logística Farmacêutica", 1L));
    }

    @Test
    void gerarMotivosDesistenciaDeveListarApenasDesistentes() {
        RelatorioResponse resposta = relatorioService.gerar(TipoRelatorio.MOTIVOS_DESISTENCIA);

        assertThat(resposta.motivosDesistencia()).hasSize(1);
        assertThat(resposta.motivosDesistencia().get(0).nome()).isEqualTo("Gabriela Pinheiro Rocha");
        assertThat(resposta.motivosDesistencia().get(0).motivo()).contains("Conflito de horário");
    }
}
