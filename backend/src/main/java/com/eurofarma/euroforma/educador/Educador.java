package com.eurofarma.euroforma.educador;

import com.eurofarma.euroforma.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "educador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Educador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PapelEducador papel;

    @Column(nullable = false)
    @Builder.Default
    private Integer turmas = 0;

    @Column(name = "ultimo_acesso")
    private Instant ultimoAcesso;
}
