package com.eurofarma.euroforma.educando;

import com.eurofarma.euroforma.curso.Curso;
import com.eurofarma.euroforma.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "educando")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Educando {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String telefone;

    private LocalDate nascimento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    @Builder.Default
    private Integer progresso = 0;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal media = BigDecimal.ZERO;

    @Column(name = "iniciado_em")
    private LocalDate iniciadoEm;

    @Embedded
    private Endereco endereco;

    private String linkedin;

    @Column(name = "curriculo_arquivo_path")
    private String curriculoArquivoPath;

    @Column(name = "curriculo_arquivo_nome_original")
    private String curriculoArquivoNomeOriginal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusEducando status = StatusEducando.INSCRITO;

    @Column(nullable = false)
    @Builder.Default
    private Integer frequencia = 0;

    @Column(name = "motivo_desistencia", columnDefinition = "text")
    private String motivoDesistencia;

    @OneToOne(mappedBy = "educando", cascade = CascadeType.ALL, orphanRemoval = true)
    private TesteIngles testeIngles;

    @OneToMany(mappedBy = "educando", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CursoAnterior> cursosAnteriores = new ArrayList<>();

    @OneToMany(mappedBy = "educando", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HistoricoEducando> historico = new ArrayList<>();

    @OneToMany(mappedBy = "educando", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoletimItem> boletim = new ArrayList<>();
}
