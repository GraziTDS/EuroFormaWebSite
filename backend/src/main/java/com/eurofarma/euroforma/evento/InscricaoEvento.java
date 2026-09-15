package com.eurofarma.euroforma.evento;

import com.eurofarma.euroforma.educando.Educando;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "inscricao_evento", uniqueConstraints = @UniqueConstraint(columnNames = {"educando_id", "evento_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "educando_id", nullable = false)
    private Educando educando;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "inscrito_em", nullable = false)
    private Instant inscritoEm;

    @PrePersist
    void onCreate() {
        if (inscritoEm == null) {
            inscritoEm = Instant.now();
        }
    }
}
