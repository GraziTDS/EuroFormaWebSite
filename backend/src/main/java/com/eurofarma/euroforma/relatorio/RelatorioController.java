package com.eurofarma.euroforma.relatorio;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RelatorioResponse gerar(@RequestParam TipoRelatorio tipo) {
        return relatorioService.gerar(tipo);
    }
}
