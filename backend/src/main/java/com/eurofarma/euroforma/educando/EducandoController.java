package com.eurofarma.euroforma.educando;

import com.eurofarma.euroforma.auditoria.AuditoriaDto;
import com.eurofarma.euroforma.educando.dto.*;
import com.eurofarma.euroforma.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/educandos")
@RequiredArgsConstructor
public class EducandoController {

    private final EducandoService educandoService;
    private final EducandoExcelService educandoExcelService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('EDUCANDO')")
    public EducandoPerfilDto meuPerfil(@AuthenticationPrincipal CustomUserDetails principal) {
        return educandoService.obterPerfilPorUsuario(principal.getId());
    }

    @PostMapping(value = "/me/curriculo", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('EDUCANDO')")
    public ResponseEntity<Void> enviarMeuCurriculo(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam("arquivo") MultipartFile arquivo) {
        Long educandoId = educandoService.obterPerfilPorUsuario(principal.getId()).id();
        educandoService.uploadCurriculo(educandoId, arquivo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public List<EducandoResumoDto> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusEducando status) {
        return educandoService.listar(busca, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public EducandoPerfilDto obter(@PathVariable Long id) {
        return educandoService.obterPerfil(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public EducandoPerfilDto cadastrar(@Valid @RequestBody CadastroEducandoRequest request) {
        return educandoService.cadastrar(request);
    }

    // A partir da Sprint 4: gerir o status/frequência de um educando (e ver seu histórico de
    // auditoria) é uma atribuição exclusiva do Administrador — o Educador/Coordenador apenas
    // acompanha a ficha do aluno em modo leitura (GET /{id} acima).
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        educandoService.atualizarStatus(id, request.status(), request.motivoDesistencia(), principal.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/frequencia")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> atualizarFrequencia(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarFrequenciaRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        educandoService.atualizarFrequencia(id, request.frequencia(), principal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/auditoria")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<AuditoriaDto> auditoria(@PathVariable Long id) {
        return educandoService.auditoria(id);
    }

    @GetMapping("/exportar-excel")
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public ResponseEntity<byte[]> exportarExcel() {
        byte[] planilha = educandoExcelService.exportar();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"educandos.xlsx\"")
                .contentType(org.springframework.http.MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(planilha);
    }

    @GetMapping("/modelo-importacao-excel")
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public ResponseEntity<byte[]> modeloImportacaoExcel() {
        byte[] planilha = educandoExcelService.gerarModeloImportacao();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"modelo-importacao-educandos.xlsx\"")
                .contentType(org.springframework.http.MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(planilha);
    }

    @PostMapping(value = "/importar-excel", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public EducandoExcelService.ResultadoImportacao importarExcel(@RequestParam("arquivo") MultipartFile arquivo) {
        return educandoExcelService.importar(arquivo);
    }

    @GetMapping("/{id}/curriculo")
    @PreAuthorize("hasAnyRole('EDUCADOR', 'COORDENADOR', 'ADMINISTRADOR')")
    public ResponseEntity<Resource> baixarCurriculo(@PathVariable Long id) {
        Resource recurso = educandoService.baixarCurriculo(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }
}
