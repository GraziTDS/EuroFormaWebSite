package com.eurofarma.euroforma.educando;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EducandoRepository extends JpaRepository<Educando, Long> {

    Optional<Educando> findByUsuarioId(Long usuarioId);

    boolean existsByCpf(String cpf);

    @Query("""
        select e from Educando e
        where (:status is null or e.status = :status)
          and (:busca is null
               or lower(e.usuario.nome) like concat('%', :busca, '%')
               or lower(e.curso.nome) like concat('%', :busca, '%'))
        order by e.usuario.nome
        """)
    List<Educando> buscar(StatusEducando status, String busca);
}
