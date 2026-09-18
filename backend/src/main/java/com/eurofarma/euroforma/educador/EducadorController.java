package com.eurofarma.euroforma.educador;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EducadorController {

    private final EducadorService educadorService;

    @GetMapping("/api/educador/dashboard")
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public EducadorDashboardDto dashboard() {
        return educadorService.dashboard();
    }

    @GetMapping("/api/educadores")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<EducadorDto> listar() {
        return educadorService.listar();
    }

    @PatchMapping("/api/educadores/{id}/ativo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> atualizarAtivo(@PathVariable Long id, @Valid @RequestBody AtivoRequest request) {
        educadorService.atualizarAtivo(id, request.ativo());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/educadores/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EducadorDto atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarEducadorRequest request) {
        return educadorService.atualizar(id, request);
    }
}
