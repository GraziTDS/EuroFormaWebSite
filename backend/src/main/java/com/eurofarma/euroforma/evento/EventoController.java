package com.eurofarma.euroforma.evento;

import com.eurofarma.euroforma.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public List<EventoDto> listar(@AuthenticationPrincipal CustomUserDetails principal) {
        return eventoService.listar(principal.getId());
    }

    @PostMapping("/{id}/inscricao")
    @PreAuthorize("hasRole('EDUCANDO')")
    public ResponseEntity<Void> inscrever(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        eventoService.inscrever(id, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
