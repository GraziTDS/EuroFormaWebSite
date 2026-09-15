package com.eurofarma.euroforma.auditoria;

import com.eurofarma.euroforma.educando.Educando;
import com.eurofarma.euroforma.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "auditoria_alteracao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Educando educando;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autor_usuario_id", nullable = false)
    private Usuario autor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CampoAuditoria campo;

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_novo")
    private String valorNovo;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    @PrePersist
    void onCreate() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
    }
}
