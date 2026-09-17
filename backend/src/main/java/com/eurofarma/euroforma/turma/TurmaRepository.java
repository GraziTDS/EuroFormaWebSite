package com.eurofarma.euroforma.turma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    List<Turma> findByEducadorIdOrderByNome(Long educadorId);

    List<Turma> findAllByOrderByNome();
}
