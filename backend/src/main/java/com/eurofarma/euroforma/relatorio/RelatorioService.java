package com.eurofarma.euroforma.relatorio;

import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.EducandoRepository;
import com.eurofarma.euroforma.educando.StatusEducando;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final EducandoRepository educandoRepository;

    public RelatorioResponse gerar(TipoRelatorio tipo) {
        List<Educando> educandos = educandoRepository.findAll();

        long total = educandos.size();
        long ativos = contar(educandos, StatusEducando.EM_CURSO);
        long concluidos = contar(educandos, StatusEducando.CONCLUIDO);
        long desistentes = contar(educandos, StatusEducando.DESISTENTE);

        List<ItemContagem> matriculasPorCurso = null;
        List<ItemFrequencia> frequenciaPorEducando = null;
        List<MotivoDesistenciaDto> motivosDesistencia = null;

        switch (tipo) {
            case MATRICULAS_POR_CURSO -> matriculasPorCurso = educandos.stream()
                    .collect(Collectors.groupingBy(e -> e.getCurso().getNome(), Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> new ItemContagem(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparingLong(ItemContagem::total).reversed())
                    .toList();
            case FREQUENCIA_POR_EDUCANDO -> frequenciaPorEducando = educandos.stream()
                    .sorted(Comparator.comparingInt(Educando::getFrequencia).reversed())
                    .map(e -> new ItemFrequencia(e.getUsuario().getNome(), e.getFrequencia()))
                    .toList();
            case CONCLUSAO_EVASAO -> {
                // stats gerais já cobrem esse relatório (ativos/concluídos/desistentes)
            }
            case MOTIVOS_DESISTENCIA -> motivosDesistencia = educandos.stream()
                    .filter(e -> e.getStatus() == StatusEducando.DESISTENTE)
                    .map(e -> new MotivoDesistenciaDto(e.getUsuario().getNome(), e.getMotivoDesistencia()))
                    .toList();
        }

        return new RelatorioResponse(tipo, total, ativos, concluidos, desistentes,
                matriculasPorCurso, frequenciaPorEducando, motivosDesistencia);
    }

    private long contar(List<Educando> educandos, StatusEducando status) {
        return educandos.stream().filter(e -> e.getStatus() == status).count();
    }
}
