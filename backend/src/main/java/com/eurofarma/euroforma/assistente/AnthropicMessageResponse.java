package com.eurofarma.euroforma.assistente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnthropicMessageResponse(List<Bloco> content) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Bloco(String type, String text) {
    }
}
