package com.eurofarma.euroforma.relatorio;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RelatorioExcelService {

    private final RelatorioService relatorioService;

    public byte[] exportar(TipoRelatorio tipo) {
        RelatorioResponse relatorio = relatorioService.gerar(tipo);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Relatório");

            CellStyle negrito = workbook.createCellStyle();
            Font fonte = workbook.createFont();
            fonte.setBold(true);
            negrito.setFont(fonte);

            int linha = 0;
            linha = escreverResumo(sheet, negrito, relatorio, linha);
            linha++;

            switch (tipo) {
                case MATRICULAS_POR_CURSO -> escreverTabela(sheet, negrito, linha,
                        new String[]{"Curso", "Total de matrículas"},
                        relatorio.matriculasPorCurso(),
                        item -> new Object[]{item.chave(), item.total()});
                case FREQUENCIA_POR_EDUCANDO -> escreverTabela(sheet, negrito, linha,
                        new String[]{"Educando", "Frequência (%)"},
                        relatorio.frequenciaPorEducando(),
                        item -> new Object[]{item.nome(), item.frequencia()});
                case CONCLUSAO_EVASAO -> escreverTabela(sheet, negrito, linha,
                        new String[]{"Indicador", "Total"},
                        java.util.List.of(
                                new ItemGenerico("Ativos", relatorio.ativos()),
                                new ItemGenerico("Concluídos", relatorio.concluidos()),
                                new ItemGenerico("Desistentes", relatorio.desistentes())),
                        item -> new Object[]{item.chave(), item.valor()});
                case MOTIVOS_DESISTENCIA -> escreverTabela(sheet, negrito, linha,
                        new String[]{"Educando", "Motivo"},
                        relatorio.motivosDesistencia(),
                        item -> new Object[]{item.nome(), item.motivo() != null ? item.motivo() : ""});
            }

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao gerar planilha do relatório", e);
        }
    }

    private int escreverResumo(Sheet sheet, CellStyle negrito, RelatorioResponse relatorio, int linhaInicial) {
        int linha = linhaInicial;
        String[][] resumo = {
                {"No filtro", String.valueOf(relatorio.noFiltro())},
                {"Ativos", String.valueOf(relatorio.ativos())},
                {"Concluídos", String.valueOf(relatorio.concluidos())},
                {"Desistentes", String.valueOf(relatorio.desistentes())},
        };
        for (String[] par : resumo) {
            Row row = sheet.createRow(linha++);
            Cell chave = row.createCell(0);
            chave.setCellValue(par[0]);
            chave.setCellStyle(negrito);
            row.createCell(1).setCellValue(par[1]);
        }
        return linha;
    }

    private <T> void escreverTabela(Sheet sheet, CellStyle negrito, int linhaInicial,
                                     String[] colunas, java.util.List<T> itens,
                                     java.util.function.Function<T, Object[]> extrator) {
        int linha = linhaInicial;
        Row cabecalho = sheet.createRow(linha++);
        for (int i = 0; i < colunas.length; i++) {
            Cell c = cabecalho.createCell(i);
            c.setCellValue(colunas[i]);
            c.setCellStyle(negrito);
        }
        if (itens == null) {
            return;
        }
        for (T item : itens) {
            Row row = sheet.createRow(linha++);
            Object[] valores = extrator.apply(item);
            for (int i = 0; i < valores.length; i++) {
                Cell cell = row.createCell(i);
                Object valor = valores[i];
                if (valor instanceof Number numero) {
                    cell.setCellValue(numero.doubleValue());
                } else {
                    cell.setCellValue(String.valueOf(valor));
                }
            }
        }
    }

    private record ItemGenerico(String chave, long valor) {
    }
}
