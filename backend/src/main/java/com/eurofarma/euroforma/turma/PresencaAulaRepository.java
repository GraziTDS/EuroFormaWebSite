package com.eurofarma.euroforma.turma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresencaAulaRepository extends JpaRepository<PresencaAula, Long> {
    List<PresencaAula> findByAulaId(Long aulaId);

    Optional<PresencaAula> findByAulaIdAndEducandoId(Long aulaId, Long educandoId);

    long countByAulaIdAndPresenteTrue(Long aulaId);
}
