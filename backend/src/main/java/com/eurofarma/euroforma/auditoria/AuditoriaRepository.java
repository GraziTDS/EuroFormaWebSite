package com.eurofarma.euroforma.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository extends JpaRepository<AuditoriaAlteracao, Long> {
    List<AuditoriaAlteracao> findByEducandoIdOrderByCriadoEmDesc(Long educandoId);
}
