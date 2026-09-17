package com.eurofarma.euroforma.assistente;

import com.eurofarma.euroforma.common.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssistenteCurriculoService {

    private static final String SYSTEM_PROMPT = """
            Você é o "Assistente de Currículo" da plataforma euroForma, do Projeto Educandos do Instituto \
            Eurofarma. Você ajuda educandos em situação de vulnerabilidade social a melhorar o currículo e a \
            se preparar para processos seletivos.

            Diretrizes:
            - Seja acolhedor, direto e prático; use frases curtas e linguagem simples (sem jargão de RH).
            - Dê sugestões concretas: como descrever experiências, verbos de ação, como destacar cursos do \
              Instituto Eurofarma, como lidar com pouca experiência profissional formal.
            - Nunca invente experiências, formações ou dados que o educando não tenha mencionado.
            - Se pedirem algo fora do tema (currículo, entrevista de emprego, carreira), redirecione \
              gentilmente de volta ao propósito da conversa.
            - Respostas objetivas, no máximo 3-4 parágrafos curtos.
            """;

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public AssistenteCurriculoService(
            @Value("${euroforma.ia.api-key:}") String apiKey,
            @Value("${euroforma.ia.model:claude-haiku-4-5}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.restClient = RestClient.builder().baseUrl("https://api.anthropic.com").build();
    }

    public boolean configurado() {
        return apiKey != null && !apiKey.isBlank();
    }

    public String responder(List<ChatMensagemDto> historico) {
        if (!configurado()) {
            throw ApiException.requisicaoInvalida(
                    "O assistente de IA ainda não foi configurado nesta instalação (variável ANTHROPIC_API_KEY).");
        }

        List<Map<String, String>> mensagens = historico.stream()
                .map(m -> {
                    Map<String, String> mensagem = new LinkedHashMap<>();
                    mensagem.put("role", m.role());
                    mensagem.put("content", m.content());
                    return mensagem;
                })
                .toList();

        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("model", model);
        corpo.put("max_tokens", 700);
        corpo.put("system", SYSTEM_PROMPT);
        corpo.put("messages", mensagens);

        try {
            AnthropicMessageResponse resposta = restClient.post()
                    .uri("/v1/messages")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpo)
                    .retrieve()
                    .body(AnthropicMessageResponse.class);

            if (resposta == null || resposta.content() == null || resposta.content().isEmpty()) {
                throw new IllegalStateException("Resposta vazia do assistente de IA");
            }
            return resposta.content().get(0).text();
        } catch (RestClientResponseException e) {
            throw new IllegalStateException("Falha ao consultar o assistente de IA: " + e.getStatusCode(), e);
        }
    }
}
