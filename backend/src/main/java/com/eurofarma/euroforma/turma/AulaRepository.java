package com.eurofarma.euroforma.turma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByTurmaIdOrderByDataDesc(Long turmaId);

    boolean existsByTurmaIdAndData(Long turmaId, java.time.LocalDate data);
}
