package com.eurofarma.euroforma.educando;

import com.eurofarma.euroforma.auditoria.AuditoriaAlteracao;
import com.eurofarma.euroforma.auditoria.AuditoriaDto;
import com.eurofarma.euroforma.auditoria.AuditoriaRepository;
import com.eurofarma.euroforma.auditoria.CampoAuditoria;
import com.eurofarma.euroforma.auth.AuthService;
import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.curso.Curso;
import com.eurofarma.euroforma.curso.CursoRepository;
import com.eurofarma.euroforma.educando.dto.*;
import com.eurofarma.euroforma.storage.StorageService;
import com.eurofarma.euroforma.usuario.Role;
import com.eurofarma.euroforma.usuario.Usuario;
import com.eurofarma.euroforma.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EducandoService {

    private static final List<String> CONTENT_TYPES_PERMITIDOS = List.of("application/pdf");

    private final EducandoRepository educandoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final StorageService storageService;

    public List<EducandoResumoDto> listar(String busca, StatusEducando status) {
        String buscaNormalizada = (busca == null || busca.isBlank()) ? null : busca.trim().toLowerCase();

        List<Educando> base = status != null
                ? educandoRepository.findByStatusOrderByUsuarioNome(status)
                : educandoRepository.findAllByOrderByUsuarioNome();

        return base.stream()
                .filter(e -> buscaNormalizada == null
                        || e.getUsuario().getNome().toLowerCase().contains(buscaNormalizada)
                        || e.getCurso().getNome().toLowerCase().contains(buscaNormalizada))
                .map(EducandoResumoDto::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public EducandoPerfilDto obterPerfil(Long id) {
        return EducandoPerfilDto.de(buscarOuFalhar(id));
    }

    @Transactional(readOnly = true)
    public EducandoPerfilDto obterPerfilPorUsuario(Long usuarioId) {
        Educando educando = educandoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> ApiException.naoEncontrado("Educando não encontrado para o usuário logado"));
        return EducandoPerfilDto.de(educando);
    }

    @Transactional
    public EducandoPerfilDto cadastrar(CadastroEducandoRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw ApiException.conflito("Já existe um usuário cadastrado com este e-mail");
        }
        if (request.cpf() != null && !request.cpf().isBlank() && educandoRepository.existsByCpf(request.cpf())) {
            throw ApiException.conflito("Já existe um educando cadastrado com este CPF");
        }
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> ApiException.requisicaoInvalida("Curso inválido"));

        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nome(request.nomeCompleto())
                .email(request.email())
                .senhaHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(Role.EDUCANDO)
                .ativo(true)
                .build());

        Educando educando = Educando.builder()
                .usuario(usuario)
                .cpf(blankToNull(request.cpf()))
                .telefone(blankToNull(request.telefone()))
                .rg(blankToNull(request.rg()))
                .nomeSocial(blankToNull(request.nomeSocial()))
                .genero(blankToNull(request.genero()))
                .raca(blankToNull(request.raca()))
                .regiao(blankToNull(request.regiao()))
                .nomeResponsavel(blankToNull(request.nomeResponsavel()))
                .contatoResponsavel(blankToNull(request.contatoResponsavel()))
                .endereco(montarEndereco(request))
                .curso(curso)
                .progresso(0)
                .media(java.math.BigDecimal.ZERO)
                .status(StatusEducando.INSCRITO)
                .frequencia(0)
                .build();
        educando.getHistorico().add(HistoricoEducando.builder()
                .educando(educando)
                .titulo("Inscrição realizada")
                .data(LocalDate.now())
                .build());

        educando = educandoRepository.save(educando);

        authService.enviarConviteDefinicaoSenha(usuario);

        return EducandoPerfilDto.de(educando);
    }

    @Transactional
    public void atualizarStatus(Long id, StatusEducando novoStatus, String motivoDesistencia, Long autorUsuarioId) {
        Educando educando = buscarOuFalhar(id);
        StatusEducando statusAnterior = educando.getStatus();

        educando.setStatus(novoStatus);
        educando.setMotivoDesistencia(novoStatus == StatusEducando.DESISTENTE ? motivoDesistencia : null);
        educandoRepository.save(educando);

        registrarAuditoria(educando, autorUsuarioId, CampoAuditoria.STATUS, statusAnterior.name(), novoStatus.name());
    }

    @Transactional
    public void atualizarFrequencia(Long id, Integer novaFrequencia, Long autorUsuarioId) {
        Educando educando = buscarOuFalhar(id);
        Integer frequenciaAnterior = educando.getFrequencia();

        educando.setFrequencia(novaFrequencia);
        educandoRepository.save(educando);

        registrarAuditoria(educando, autorUsuarioId, CampoAuditoria.FREQUENCIA, String.valueOf(frequenciaAnterior), String.valueOf(novaFrequencia));
    }

    public List<AuditoriaDto> auditoria(Long id) {
        buscarOuFalhar(id);
        return auditoriaRepository.findByEducandoIdOrderByCriadoEmDesc(id).stream()
                .map(AuditoriaDto::de)
                .toList();
    }

    @Transactional
    public void uploadCurriculo(Long educandoId, MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw ApiException.requisicaoInvalida("Selecione um arquivo para enviar");
        }
        if (!CONTENT_TYPES_PERMITIDOS.contains(arquivo.getContentType())) {
            throw ApiException.requisicaoInvalida("Apenas arquivos PDF são aceitos");
        }

        Educando educando = buscarOuFalhar(educandoId);
        String path = storageService.armazenar(arquivo, "curriculo-" + educandoId);

        educando.setCurriculoArquivoPath(path);
        educando.setCurriculoArquivoNomeOriginal(arquivo.getOriginalFilename());
        educandoRepository.save(educando);
    }

    public Resource baixarCurriculo(Long educandoId) {
        Educando educando = buscarOuFalhar(educandoId);
        if (educando.getCurriculoArquivoPath() == null) {
            throw ApiException.naoEncontrado("Este educando ainda não enviou um currículo");
        }
        return storageService.carregar(educando.getCurriculoArquivoPath());
    }

    public Educando buscarOuFalhar(Long id) {
        return educandoRepository.findById(id)
                .orElseThrow(() -> ApiException.naoEncontrado("Educando não encontrado: " + id));
    }

    private void registrarAuditoria(Educando educando, Long autorUsuarioId, CampoAuditoria campo, String anterior, String novo) {
        auditoriaRepository.save(AuditoriaAlteracao.builder()
                .educando(educando)
                .autor(usuarioRepository.getReferenceById(autorUsuarioId))
                .campo(campo)
                .valorAnterior(anterior)
                .valorNovo(novo)
                .build());
    }

    private String blankToNull(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor;
    }

    private Endereco montarEndereco(CadastroEducandoRequest request) {
        String cep = blankToNull(request.cep());
        String rua = blankToNull(request.rua());
        String bairro = blankToNull(request.bairro());
        String cidade = blankToNull(request.cidade());
        if (cep == null && rua == null && bairro == null && cidade == null) {
            return null;
        }
        return Endereco.builder().cep(cep).rua(rua).bairro(bairro).cidade(cidade).build();
    }
}
