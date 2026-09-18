package com.eurofarma.euroforma.educando;

import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.curso.Curso;
import com.eurofarma.euroforma.curso.CursoRepository;
import com.eurofarma.euroforma.educando.dto.CadastroEducandoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Import/export de educandos em Excel, com o modelo/cabeçalho alinhado à planilha institucional
 * "BASE DE DADOS EDUCACIONAL" já usada pelo Instituto Eurofarma — o reconhecimento de colunas é feito
 * pelo NOME do cabeçalho (não pela posição), ignorando maiúsculas/minúsculas, acentos e ":" no final,
 * para aceitar tanto o modelo gerado aqui quanto a planilha real da instituição.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EducandoExcelService {

    private static final List<String> COLUNAS_EXPORTACAO = List.of(
            "Nome", "E-mail", "CPF", "Telefone", "Curso", "Turma", "Status", "Frequência (%)", "Progresso (%)", "Média",
            "RG", "Nome social", "Gênero", "Raça", "Região", "Nome do responsável", "Contato do responsável");

    private static final List<String> COLUNAS_MODELO = List.of(
            "Nome completo:", "CPF:", "RG:", "Nome Social:", "Gênero:", "Raça:", "Telefone WhatsApp:",
            "E-mail (opcional):", "Curso:", "Região:", "Nome do Responsável:", "Contato do Responsável:",
            "CEP:", "Rua:", "Bairro:", "Cidade:");

    /** Cada campo aceita várias grafias de cabeçalho (com/sem acento, com/sem ":", inclusive o erro de digitação
     *  "Resposável" que existe na planilha oficial do Instituto). */
    private static final Map<String, List<String>> SINONIMOS_CABECALHO = Map.ofEntries(
            Map.entry("nome", List.of("nome completo", "nome", "nome completo do(a) candidato(a)", "nome do aluno")),
            Map.entry("cpf", List.of("cpf", "para comecar, digite seu cpf no campo abaixo")),
            Map.entry("rg", List.of("rg")),
            Map.entry("nomeSocial", List.of("nome social", "nome social (se houver)")),
            Map.entry("genero", List.of("genero", "genero (se houver)")),
            Map.entry("raca", List.of("raca", "qual sua raca/etnia? (autodeclaracao)")),
            Map.entry("telefone", List.of("telefone whatsapp", "telefone", "celular/whatsapp", "celular")),
            Map.entry("email", List.of("e-mail", "email", "e-mail (opcional)", "endereco de e-mail")),
            Map.entry("curso", List.of("curso")),
            Map.entry("regiao", List.of("regiao", "regiao onde mora")),
            Map.entry("nomeResponsavel", List.of("nome do responsavel", "nome do resposavel")),
            Map.entry("contatoResponsavel", List.of("contato do responsavel", "contato do responsavel legal (ou emergencia)")),
            Map.entry("cep", List.of("cep")),
            Map.entry("rua", List.of("rua", "nome da rua")),
            Map.entry("bairro", List.of("bairro")),
            Map.entry("cidade", List.of("cidade"))
    );

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
                row.createCell(10).setCellValue(vazioSeNulo(e.getRg()));
                row.createCell(11).setCellValue(vazioSeNulo(e.getNomeSocial()));
                row.createCell(12).setCellValue(vazioSeNulo(e.getGenero()));
                row.createCell(13).setCellValue(vazioSeNulo(e.getRaca()));
                row.createCell(14).setCellValue(vazioSeNulo(e.getRegiao()));
                row.createCell(15).setCellValue(vazioSeNulo(e.getNomeResponsavel()));
                row.createCell(16).setCellValue(vazioSeNulo(e.getContatoResponsavel()));
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
     * Planilha-modelo pronta para preencher e importar, com o mesmo cabeçalho usado pela planilha
     * "BASE DE DADOS EDUCACIONAL" do Instituto — inclusive é possível reaproveitar a planilha institucional
     * já preenchida diretamente em {@link #importar}, já que as colunas são lidas pelo nome.
     */
    public byte[] gerarModeloImportacao() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Modelo");

            CellStyle estiloCabecalho = workbook.createCellStyle();
            Font fonteCabecalho = workbook.createFont();
            fonteCabecalho.setBold(true);
            estiloCabecalho.setFont(fonteCabecalho);

            Row cabecalho = sheet.createRow(0);
            for (int i = 0; i < COLUNAS_MODELO.size(); i++) {
                Cell celula = cabecalho.createCell(i);
                celula.setCellValue(COLUNAS_MODELO.get(i));
                celula.setCellStyle(estiloCabecalho);
            }

            String primeiroCurso = cursoRepository.findAll().stream()
                    .findFirst()
                    .map(Curso::getNome)
                    .orElse("Auxiliar de Farmácia");

            Row exemplo = sheet.createRow(1);
            exemplo.createCell(0).setCellValue("Maria da Silva");
            exemplo.createCell(1).setCellValue("123.456.789-00");
            exemplo.createCell(2).setCellValue("12.345.678-9");
            exemplo.createCell(3).setCellValue("");
            exemplo.createCell(4).setCellValue("Feminino");
            exemplo.createCell(5).setCellValue("Parda");
            exemplo.createCell(6).setCellValue("(11) 90000-0000");
            exemplo.createCell(7).setCellValue("maria.silva@email.com");
            exemplo.createCell(8).setCellValue(primeiroCurso);
            exemplo.createCell(9).setCellValue("Zona Leste");
            exemplo.createCell(10).setCellValue("João da Silva");
            exemplo.createCell(11).setCellValue("(11) 90000-0001");
            exemplo.createCell(12).setCellValue("03000-000");
            exemplo.createCell(13).setCellValue("Rua das Flores");
            exemplo.createCell(14).setCellValue("Centro");
            exemplo.createCell(15).setCellValue("São Paulo");

            for (int i = 0; i < COLUNAS_MODELO.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao gerar planilha-modelo", e);
        }
    }

    /**
     * Lê as colunas pelo NOME do cabeçalho (linha 1), não pela posição — assim aceita tanto o modelo
     * gerado por {@link #gerarModeloImportacao} quanto a planilha "BASE DE DADOS EDUCACIONAL" real do
     * Instituto. "Nome completo" e "Curso" são obrigatórios; os demais campos institucionais são opcionais.
     * A planilha oficial da instituição não tem coluna de e-mail (necessário para login) — quando ausente,
     * um e-mail temporário é gerado a partir do CPF e um aviso é retornado pedindo a atualização posterior.
     */
    public ResultadoImportacao importar(MultipartFile arquivo) {
        List<LinhaImportada> linhas = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(arquivo.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            Row linhaCabecalho = sheet.getRow(0);
            if (linhaCabecalho == null) {
                throw ApiException.requisicaoInvalida("A planilha está vazia");
            }
            Map<String, Integer> colunas = mapearColunas(linhaCabecalho, formatter);

            Integer colNome = colunas.get("nome");
            Integer colCurso = colunas.get("curso");
            if (colNome == null || colCurso == null) {
                throw ApiException.requisicaoInvalida(
                        "Não foi possível identificar as colunas obrigatórias 'Nome completo' e 'Curso' no cabeçalho da planilha");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || estaVazia(row, colNome, formatter)) {
                    continue;
                }

                int numeroLinha = i + 1;
                String nome = valor(row, colunas, "nome", formatter);
                String cpf = valor(row, colunas, "cpf", formatter);
                String cursoNome = valor(row, colunas, "curso", formatter);
                boolean emailTemporario = false;
                String email = valor(row, colunas, "email", formatter);

                try {
                    if (email == null || email.isBlank()) {
                        email = gerarEmailTemporario(cpf);
                        emailTemporario = true;
                    }

                    Curso curso = cursoRepository.findAll().stream()
                            .filter(c -> c.getNome().equalsIgnoreCase(cursoNome))
                            .findFirst()
                            .orElseThrow(() -> ApiException.requisicaoInvalida("Curso não encontrado: " + cursoNome));

                    educandoService.cadastrar(new CadastroEducandoRequest(
                            nome,
                            cpf,
                            valor(row, colunas, "telefone", formatter),
                            email,
                            curso.getId(),
                            valor(row, colunas, "rg", formatter),
                            valor(row, colunas, "nomeSocial", formatter),
                            valor(row, colunas, "genero", formatter),
                            valor(row, colunas, "raca", formatter),
                            valor(row, colunas, "regiao", formatter),
                            valor(row, colunas, "nomeResponsavel", formatter),
                            valor(row, colunas, "contatoResponsavel", formatter),
                            valor(row, colunas, "cep", formatter),
                            valor(row, colunas, "rua", formatter),
                            valor(row, colunas, "bairro", formatter),
                            valor(row, colunas, "cidade", formatter)));

                    linhas.add(new LinhaImportada(numeroLinha, nome, cpf, cursoNome, email, emailTemporario, null));
                } catch (Exception ex) {
                    linhas.add(new LinhaImportada(numeroLinha, nome, cpf, cursoNome, null, false, mensagemAmigavel(ex, numeroLinha)));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler a planilha enviada", e);
        }

        int criados = (int) linhas.stream().filter(l -> l.erro() == null).count();
        return new ResultadoImportacao(criados, linhas);
    }

    private Map<String, Integer> mapearColunas(Row linhaCabecalho, DataFormatter formatter) {
        Map<String, Integer> colunas = new LinkedHashMap<>();
        for (int c = 0; c < linhaCabecalho.getLastCellNum(); c++) {
            Cell celula = linhaCabecalho.getCell(c);
            if (celula == null) {
                continue;
            }
            String normalizado = normalizar(formatter.formatCellValue(celula));
            if (normalizado.isBlank()) {
                continue;
            }
            for (Map.Entry<String, List<String>> entry : SINONIMOS_CABECALHO.entrySet()) {
                if (!colunas.containsKey(entry.getKey()) && entry.getValue().contains(normalizado)) {
                    colunas.put(entry.getKey(), c);
                    break;
                }
            }
        }
        return colunas;
    }

    private String valor(Row row, Map<String, Integer> colunas, String campo, DataFormatter formatter) {
        Integer indice = colunas.get(campo);
        if (indice == null) {
            return null;
        }
        String valor = formatter.formatCellValue(row.getCell(indice)).trim();
        return valor.isBlank() ? null : valor;
    }

    /**
     * Mensagens de {@link ApiException} já são escritas para o usuário final (ex.: "Curso não encontrado: X")
     * e são exibidas como estão. Qualquer outra falha (erro técnico/infraestrutura, ex.: banco ou e-mail fora do
     * ar) é registrada no log do servidor e traduzida para uma mensagem genérica, para não expor detalhes internos.
     */
    private String mensagemAmigavel(Exception ex, int numeroLinha) {
        if (ex instanceof ApiException) {
            return ex.getMessage();
        }
        log.error("Falha inesperada ao importar a linha {} da planilha", numeroLinha, ex);
        return "Não foi possível importar esta linha por um erro interno do sistema. Tente novamente ou contate o suporte.";
    }

    private String gerarEmailTemporario(String cpf) {
        String digitos = cpf == null ? "" : cpf.replaceAll("\\D", "");
        String identificador = digitos.isBlank() ? UUID.randomUUID().toString().substring(0, 8) : digitos;
        return "pendente." + identificador + "@educando.euroforma.local";
    }

    private String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento
                .toLowerCase()
                .trim()
                .replaceAll(":\\s*$", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean estaVazia(Row row, int colNome, DataFormatter formatter) {
        Cell celulaNome = row.getCell(colNome);
        return celulaNome == null || formatter.formatCellValue(celulaNome).isBlank();
    }

    private String vazioSeNulo(String valor) {
        return valor == null ? "" : valor;
    }

    /** Uma linha da planilha importada, com os dados que foram usados no cadastro (para revisão na tela) e,
     *  em caso de falha, a mensagem de erro — {@code erro} é {@code null} quando a linha foi importada com sucesso. */
    public record LinhaImportada(
            int linha,
            String nome,
            String cpf,
            String curso,
            String email,
            boolean emailTemporario,
            String erro
    ) {
    }

    public record ResultadoImportacao(int criados, List<LinhaImportada> linhas) {
    }
}
