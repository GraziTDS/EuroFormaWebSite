package com.eurofarma.euroforma.educando;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "teste_ingles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TesteIngles {

    @Id
    @Column(name = "educando_id")
    private Long educandoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "educando_id")
    private Educando educando;

    @Column(nullable = false, length = 10)
    private String nivel;

    @Column(nullable = false)
    private Integer pontuacao;

    @Column(name = "data_realizacao", nullable = false)
    private LocalDate data;
}
