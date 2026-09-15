package com.eurofarma.euroforma.evento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoEventoRepository extends JpaRepository<InscricaoEvento, Long> {

    boolean existsByEducandoIdAndEventoId(Long educandoId, Long eventoId);

    long countByEventoId(Long eventoId);

    List<InscricaoEvento> findByEducandoId(Long educandoId);

    Optional<InscricaoEvento> findByEducandoIdAndEventoId(Long educandoId, Long eventoId);
}
