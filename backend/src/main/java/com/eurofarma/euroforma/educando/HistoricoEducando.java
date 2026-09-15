package com.eurofarma.euroforma.educando;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "historico_educando")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoEducando {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Educando educando;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "data_evento", nullable = false)
    private LocalDate data;
}
