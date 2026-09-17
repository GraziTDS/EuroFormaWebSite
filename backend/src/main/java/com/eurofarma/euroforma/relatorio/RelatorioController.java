package com.eurofarma.euroforma.relatorio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final RelatorioExcelService relatorioExcelService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RelatorioResponse gerar(@RequestParam TipoRelatorio tipo) {
        return relatorioService.gerar(tipo);
    }

    @GetMapping("/exportar-excel")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<byte[]> exportarExcel(@RequestParam TipoRelatorio tipo) {
        byte[] planilha = relatorioExcelService.exportar(tipo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(planilha);
    }
}
