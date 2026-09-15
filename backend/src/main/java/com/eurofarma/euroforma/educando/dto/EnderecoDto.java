package com.eurofarma.euroforma.educando.dto;

import com.eurofarma.euroforma.educando.Endereco;

public record EnderecoDto(String rua, String bairro, String cep, String cidade, String uf) {
    public static EnderecoDto de(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoDto(endereco.getRua(), endereco.getBairro(), endereco.getCep(), endereco.getCidade(), endereco.getUf());
    }
}
