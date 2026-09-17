package com.eurofarma.euroforma.turma;

import com.eurofarma.euroforma.security.CustomUserDetails;
import com.eurofarma.euroforma.turma.dto.AulaDto;
import com.eurofarma.euroforma.turma.dto.CriarAulaRequest;
import com.eurofarma.euroforma.turma.dto.PresencaItemDto;
import com.eurofarma.euroforma.turma.dto.SalvarChamadaRequest;
import com.eurofarma.euroforma.turma.dto.TurmaDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR')")
public class TurmaController {

    private final TurmaService turmaService;

    @GetMapping("/turmas")
    public List<TurmaDto> listar(@AuthenticationPrincipal CustomUserDetails principal) {
        return turmaService.listar(principal.getId());
    }

    @GetMapping("/turmas/{id}/aulas")
    public List<AulaDto> listarAulas(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        return turmaService.listarAulas(id, principal.getId());
    }

    @PostMapping("/turmas/{id}/aulas")
    public AulaDto criarAula(
            @PathVariable Long id,
            @Valid @RequestBody CriarAulaRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return turmaService.criarAula(id, request, principal.getId());
    }

    @GetMapping("/aulas/{aulaId}/chamada")
    public List<PresencaItemDto> obterChamada(@PathVariable Long aulaId, @AuthenticationPrincipal CustomUserDetails principal) {
        return turmaService.obterChamada(aulaId, principal.getId());
    }

    @PutMapping("/aulas/{aulaId}/chamada")
    public ResponseEntity<Void> salvarChamada(
            @PathVariable Long aulaId,
            @Valid @RequestBody SalvarChamadaRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        turmaService.salvarChamada(aulaId, request, principal.getId());
        return ResponseEntity.noContent().build();
    }
}
