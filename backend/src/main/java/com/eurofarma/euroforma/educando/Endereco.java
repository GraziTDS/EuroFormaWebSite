package com.eurofarma.euroforma.educando;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Column(name = "endereco_rua")
    private String rua;

    @Column(name = "endereco_bairro")
    private String bairro;

    @Column(name = "endereco_cep")
    private String cep;

    @Column(name = "endereco_cidade")
    private String cidade;

    @Column(name = "endereco_uf", length = 2)
    private String uf;
}
