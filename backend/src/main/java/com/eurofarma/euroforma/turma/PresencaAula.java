package com.eurofarma.euroforma.turma;

import com.eurofarma.euroforma.educando.Educando;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "presenca_aula", uniqueConstraints = @UniqueConstraint(columnNames = {"aula_id", "educando_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresencaAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Educando educando;

    @Column(nullable = false)
    @Builder.Default
    private boolean presente = true;
}
