package com.eurofarma.euroforma.turma;

import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.EducandoRepository;
import com.eurofarma.euroforma.educador.Educador;
import com.eurofarma.euroforma.educador.EducadorRepository;
import com.eurofarma.euroforma.turma.dto.AulaDto;
import com.eurofarma.euroforma.turma.dto.CriarAulaRequest;
import com.eurofarma.euroforma.turma.dto.PresencaItemDto;
import com.eurofarma.euroforma.turma.dto.SalvarChamadaRequest;
import com.eurofarma.euroforma.turma.dto.TurmaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A chamada é uma atribuição exclusiva do Educador/Coordenador responsável pela turma —
 * o Administrador não tem acesso a este módulo (ver Sprint 4/5).
 */
@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AulaRepository aulaRepository;
    private final PresencaAulaRepository presencaAulaRepository;
    private final EducandoRepository educandoRepository;
    private final EducadorRepository educadorRepository;

    @Transactional(readOnly = true)
    public List<TurmaDto> listar(Long usuarioLogadoId) {
        List<Turma> turmas = turmaRepository.findByEducadorIdOrderByNome(resolverEducador(usuarioLogadoId).getId());
        return turmas.stream().map(this::paraDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AulaDto> listarAulas(Long turmaId, Long usuarioLogadoId) {
        Turma turma = buscarTurmaComPermissao(turmaId, usuarioLogadoId);
        return aulaRepository.findByTurmaIdOrderByDataDesc(turma.getId()).stream()
                .map(this::paraDto)
                .toList();
    }

    @Transactional
    public AulaDto criarAula(Long turmaId, CriarAulaRequest request, Long usuarioLogadoId) {
        Turma turma = buscarTurmaComPermissao(turmaId, usuarioLogadoId);

        if (aulaRepository.existsByTurmaIdAndData(turmaId, request.data())) {
            throw ApiException.conflito("Já existe uma aula registrada para esta turma nesta data");
        }

        Aula aula = aulaRepository.save(Aula.builder()
                .turma(turma)
                .data(request.data())
                .tema(request.tema())
                .build());

        List<Educando> matriculados = educandoRepository.findByTurmaIdOrderByUsuarioNome(turma.getId());
        List<PresencaAula> presencas = matriculados.stream()
                .map(educando -> PresencaAula.builder().aula(aula).educando(educando).presente(true).build())
                .toList();
        presencaAulaRepository.saveAll(presencas);

        return paraDto(aula);
    }

    @Transactional(readOnly = true)
    public List<PresencaItemDto> obterChamada(Long aulaId, Long usuarioLogadoId) {
        Aula aula = buscarAulaComPermissao(aulaId, usuarioLogadoId);

        Map<Long, Boolean> presencasRegistradas = presencaAulaRepository.findByAulaId(aulaId).stream()
                .collect(Collectors.toMap(p -> p.getEducando().getId(), PresencaAula::isPresente));

        return educandoRepository.findByTurmaIdOrderByUsuarioNome(aula.getTurma().getId()).stream()
                .map(educando -> new PresencaItemDto(
                        educando.getId(),
                        educando.getUsuario().getNome(),
                        presencasRegistradas.getOrDefault(educando.getId(), true)))
                .toList();
    }

    @Transactional
    public void salvarChamada(Long aulaId, SalvarChamadaRequest request, Long usuarioLogadoId) {
        Aula aula = buscarAulaComPermissao(aulaId, usuarioLogadoId);

        for (SalvarChamadaRequest.ItemRequest item : request.presencas()) {
            PresencaAula presenca = presencaAulaRepository.findByAulaIdAndEducandoId(aulaId, item.educandoId())
                    .orElseGet(() -> PresencaAula.builder()
                            .aula(aula)
                            .educando(educandoRepository.getReferenceById(item.educandoId()))
                            .build());
            presenca.setPresente(item.presente());
            presencaAulaRepository.save(presenca);
        }
    }

    private Turma buscarTurmaComPermissao(Long turmaId, Long usuarioLogadoId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> ApiException.naoEncontrado("Turma não encontrada: " + turmaId));
        if (!turma.getEducador().getId().equals(resolverEducador(usuarioLogadoId).getId())) {
            throw ApiException.proibido("Você não tem acesso a esta turma");
        }
        return turma;
    }

    private Aula buscarAulaComPermissao(Long aulaId, Long usuarioLogadoId) {
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> ApiException.naoEncontrado("Aula não encontrada: " + aulaId));
        buscarTurmaComPermissao(aula.getTurma().getId(), usuarioLogadoId);
        return aula;
    }

    private Educador resolverEducador(Long usuarioLogadoId) {
        return educadorRepository.findByUsuarioId(usuarioLogadoId)
                .orElseThrow(() -> ApiException.proibido("Apenas educadores/coordenadores acessam turmas"));
    }

    private TurmaDto paraDto(Turma turma) {
        long total = educandoRepository.findByTurmaIdOrderByUsuarioNome(turma.getId()).size();
        return new TurmaDto(turma.getId(), turma.getNome(), turma.getCurso().getNome(),
                turma.getEducador().getUsuario().getNome(), total);
    }

    private AulaDto paraDto(Aula aula) {
        long totalEducandos = educandoRepository.findByTurmaIdOrderByUsuarioNome(aula.getTurma().getId()).size();
        long totalPresentes = presencaAulaRepository.countByAulaIdAndPresenteTrue(aula.getId());
        return new AulaDto(aula.getId(), aula.getData(), aula.getTema(), totalPresentes, totalEducandos);
    }
}
