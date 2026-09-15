package com.eurofarma.euroforma.educador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducadorRepository extends JpaRepository<Educador, Long> {
    Optional<Educador> findByUsuarioId(Long usuarioId);
}
