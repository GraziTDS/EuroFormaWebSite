package com.eurofarma.euroforma.assistente;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistente/curriculo")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EDUCANDO')")
public class AssistenteCurriculoController {

    private final AssistenteCurriculoService assistenteCurriculoService;

    @GetMapping("/disponivel")
    public DisponibilidadeDto disponivel() {
        return new DisponibilidadeDto(assistenteCurriculoService.configurado());
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return new ChatResponse(assistenteCurriculoService.responder(request.historico()));
    }

    public record DisponibilidadeDto(boolean disponivel) {
    }
}
