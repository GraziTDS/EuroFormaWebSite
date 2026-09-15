package com.eurofarma.euroforma.educando;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "boletim_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoletimItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Educando educando;

    @Column(name = "disciplina_nome", nullable = false)
    private String disciplinaNome;

    private BigDecimal cp1;
    private BigDecimal gs1;
    private BigDecimal md1;
    private BigDecimal cp2;
    private BigDecimal gs2;
    private BigDecimal md2;

    @Column(nullable = false)
    private Integer faltas;

    @Column(name = "presencas_total", nullable = false)
    private Integer presencasTotal;

    @Column(name = "presencas_realizadas", nullable = false)
    private Integer presencasRealizadas;

    @Column(name = "frequencia_percentual", nullable = false)
    private BigDecimal frequenciaPercentual;

    @Column(name = "media_parcial")
    private BigDecimal mediaParcial;
}
