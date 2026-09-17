package com.eurofarma.euroforma.educando;

import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.curso.Curso;
import com.eurofarma.euroforma.curso.CursoRepository;
import com.eurofarma.euroforma.educando.dto.CadastroEducandoRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EducandoExcelService {

    private static final List<String> COLUNAS_EXPORTACAO = List.of(
            "Nome", "E-mail", "CPF", "Telefone", "Curso", "Turma", "Status", "Frequência (%)", "Progresso (%)", "Média");

    private final EducandoRepository educandoRepository;
    private final EducandoService educandoService;
    private final CursoRepository cursoRepository;

    public byte[] exportar() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Educandos");

            CellStyle estiloCabecalho = workbook.createCellStyle();
            Font fonteCabecalho = workbook.createFont();
            fonteCabecalho.setBold(true);
            estiloCabecalho.setFont(fonteCabecalho);

            Row cabecalho = sheet.createRow(0);
            for (int i = 0; i < COLUNAS_EXPORTACAO.size(); i++) {
                Cell celula = cabecalho.createCell(i);
                celula.setCellValue(COLUNAS_EXPORTACAO.get(i));
                celula.setCellStyle(estiloCabecalho);
            }

            List<Educando> educandos = educandoRepository.findAllByOrderByUsuarioNome();
            int linha = 1;
            for (Educando e : educandos) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(e.getUsuario().getNome());
                row.createCell(1).setCellValue(e.getUsuario().getEmail());
                row.createCell(2).setCellValue(e.getCpf());
                row.createCell(3).setCellValue(e.getTelefone());
                row.createCell(4).setCellValue(e.getCurso().getNome());
                row.createCell(5).setCellValue(e.getTurma() != null ? e.getTurma().getNome() : "");
                row.createCell(6).setCellValue(e.getStatus().name());
                row.createCell(7).setCellValue(e.getFrequencia());
                row.createCell(8).setCellValue(e.getProgresso());
                row.createCell(9).setCellValue(e.getMedia().doubleValue());
            }

            for (int i = 0; i < COLUNAS_EXPORTACAO.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao gerar planilha de educandos", e);
        }
    }

    /**
     * Espera colunas na ordem: Nome completo | CPF | Telefone | E-mail | Curso (com cabeçalho na primeira linha).
     */
    public ResultadoImportacao importar(MultipartFile arquivo) {
        List<String> erros = new ArrayList<>();
        int criados = 0;

        try (Workbook workbook = new XSSFWorkbook(arquivo.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || estaVazia(row, formatter)) {
                    continue;
                }

                String nome = formatter.formatCellValue(row.getCell(0)).trim();
                String cpf = formatter.formatCellValue(row.getCell(1)).trim();
                String telefone = formatter.formatCellValue(row.getCell(2)).trim();
                String email = formatter.formatCellValue(row.getCell(3)).trim();
                String cursoNome = formatter.formatCellValue(row.getCell(4)).trim();

                try {
                    Curso curso = cursoRepository.findAll().stream()
                            .filter(c -> c.getNome().equalsIgnoreCase(cursoNome))
                            .findFirst()
                            .orElseThrow(() -> ApiException.requisicaoInvalida("Curso não encontrado: " + cursoNome));

                    educandoService.cadastrar(new CadastroEducandoRequest(nome, cpf, telefone, email, curso.getId()));
                    criados++;
                } catch (Exception ex) {
                    erros.add("Linha " + (i + 1) + " (" + nome + "): " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler a planilha enviada", e);
        }

        return new ResultadoImportacao(criados, erros);
    }

    private boolean estaVazia(Row row, DataFormatter formatter) {
        Cell primeiraCelula = row.getCell(0);
        return primeiraCelula == null || formatter.formatCellValue(primeiraCelula).isBlank();
    }

    public record ResultadoImportacao(int criados, List<String> erros) {
    }
}
