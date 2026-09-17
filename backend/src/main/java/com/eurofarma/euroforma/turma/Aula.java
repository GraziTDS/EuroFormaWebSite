package com.eurofarma.euroforma.turma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "aula", uniqueConstraints = @UniqueConstraint(columnNames = {"turma_id", "data_aula"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(name = "data_aula", nullable = false)
    private LocalDate data;

    private String tema;
}
