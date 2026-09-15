package com.eurofarma.euroforma.admin;

import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.EducandoRepository;
import com.eurofarma.euroforma.educando.StatusEducando;
import com.eurofarma.euroforma.educador.Educador;
import com.eurofarma.euroforma.educador.EducadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EducandoRepository educandoRepository;
    private final EducadorRepository educadorRepository;

    public AdminVisaoGeralDto visaoGeral() {
        List<Educando> educandos = educandoRepository.findAll();
        List<Educador> educadores = educadorRepository.findAll();

        long total = educandos.size();
        long ativos = educandos.stream().filter(e -> e.getStatus() == StatusEducando.EM_CURSO).count();
        long concluidos = educandos.stream().filter(e -> e.getStatus() == StatusEducando.CONCLUIDO).count();
        int taxaConclusao = total == 0 ? 0 : (int) (concluidos * 100 / total);
        int presencaMedia = total == 0 ? 0
                : (int) Math.round(educandos.stream().mapToInt(Educando::getFrequencia).average().orElse(0));

        long educadoresAtivos = educadores.stream().filter(e -> e.getUsuario().isAtivo()).count();

        Map<StatusEducando, Long> distribuicao = educandos.stream()
                .collect(Collectors.groupingBy(Educando::getStatus, Collectors.counting()));

        Map<String, Long> porCurso = educandos.stream()
                .collect(Collectors.groupingBy(e -> e.getCurso().getNome(), Collectors.counting()));

        return new AdminVisaoGeralDto(
                total, ativos, presencaMedia, taxaConclusao, concluidos,
                educadoresAtivos, educadores.size(), distribuicao, porCurso);
    }
}
