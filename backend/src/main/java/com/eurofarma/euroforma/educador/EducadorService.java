package com.eurofarma.euroforma.educador;

import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.EducandoRepository;
import com.eurofarma.euroforma.educando.StatusEducando;
import com.eurofarma.euroforma.educando.dto.EducandoResumoDto;
import com.eurofarma.euroforma.usuario.Usuario;
import com.eurofarma.euroforma.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducadorService {

    private final EducadorRepository educadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final EducandoRepository educandoRepository;

    public List<EducadorDto> listar() {
        return educadorRepository.findAll(Sort.by("id")).stream()
                .map(EducadorDto::de)
                .toList();
    }

    @Transactional
    public void atualizarAtivo(Long id, boolean ativo) {
        Educador educador = educadorRepository.findById(id)
                .orElseThrow(() -> ApiException.naoEncontrado("Educador não encontrado: " + id));
        Usuario usuario = educador.getUsuario();
        usuario.setAtivo(ativo);
        usuarioRepository.save(usuario);
    }

    public EducadorDashboardDto dashboard() {
        List<Educando> educandos = educandoRepository.findAll();

        long total = educandos.size();
        long ativos = contar(educandos, StatusEducando.EM_CURSO);
        long concluintes = contar(educandos, StatusEducando.CONCLUIDO);
        int taxaConclusao = total == 0 ? 0 : (int) (concluintes * 100 / total);

        Map<StatusEducando, Long> distribuicao = educandos.stream()
                .collect(Collectors.groupingBy(Educando::getStatus, Collectors.counting()));

        List<EducandoResumoDto> recentes = educandos.stream()
                .sorted(Comparator.comparing(Educando::getId).reversed())
                .limit(5)
                .map(EducandoResumoDto::de)
                .toList();

        return new EducadorDashboardDto(total, ativos, concluintes, taxaConclusao, distribuicao, recentes);
    }

    private long contar(List<Educando> educandos, StatusEducando status) {
        return educandos.stream().filter(e -> e.getStatus() == status).count();
    }
}
