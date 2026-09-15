package com.eurofarma.euroforma.evento;

import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.educando.EducandoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final InscricaoEventoRepository inscricaoEventoRepository;
    private final EducandoRepository educandoRepository;

    public List<EventoDto> listar(Long usuarioLogadoId) {
        Long educandoId = usuarioLogadoId == null ? null
                : educandoRepository.findByUsuarioId(usuarioLogadoId).map(Educando::getId).orElse(null);

        return eventoRepository.findAll().stream()
                .map(evento -> {
                    long ocupadas = inscricaoEventoRepository.countByEventoId(evento.getId());
                    boolean inscrito = educandoId != null
                            && inscricaoEventoRepository.existsByEducandoIdAndEventoId(educandoId, evento.getId());
                    return new EventoDto(
                            evento.getId(), evento.getTipo(), evento.getTitulo(), evento.getDescricao(),
                            evento.getLocal(), evento.getDataHora(), evento.getVagasTotal(), ocupadas, inscrito);
                })
                .toList();
    }

    @Transactional
    public void inscrever(Long eventoId, Long usuarioLogadoId) {
        Educando educando = educandoRepository.findByUsuarioId(usuarioLogadoId)
                .orElseThrow(() -> ApiException.naoEncontrado("Educando não encontrado para o usuário logado"));
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> ApiException.naoEncontrado("Evento não encontrado: " + eventoId));

        if (inscricaoEventoRepository.existsByEducandoIdAndEventoId(educando.getId(), eventoId)) {
            throw ApiException.conflito("Você já está inscrito neste evento");
        }
        long ocupadas = inscricaoEventoRepository.countByEventoId(eventoId);
        if (ocupadas >= evento.getVagasTotal()) {
            throw ApiException.conflito("Este evento não possui mais vagas disponíveis");
        }

        inscricaoEventoRepository.save(InscricaoEvento.builder()
                .educando(educando)
                .evento(evento)
                .build());
    }
}
