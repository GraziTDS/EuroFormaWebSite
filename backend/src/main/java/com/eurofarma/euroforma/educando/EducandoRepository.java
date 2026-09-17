package com.eurofarma.euroforma.educando;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducandoRepository extends JpaRepository<Educando, Long> {

    Optional<Educando> findByUsuarioId(Long usuarioId);

    boolean existsByCpf(String cpf);

    List<Educando> findByStatusOrderByUsuarioNome(StatusEducando status);

    List<Educando> findAllByOrderByUsuarioNome();

    List<Educando> findByTurmaIdOrderByUsuarioNome(Long turmaId);
}
