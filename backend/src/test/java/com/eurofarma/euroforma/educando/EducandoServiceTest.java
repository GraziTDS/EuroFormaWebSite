package com.eurofarma.euroforma.educando;

import com.eurofarma.euroforma.auditoria.AuditoriaAlteracao;
import com.eurofarma.euroforma.auditoria.AuditoriaRepository;
import com.eurofarma.euroforma.auditoria.CampoAuditoria;
import com.eurofarma.euroforma.auth.AuthService;
import com.eurofarma.euroforma.curso.Curso;
import com.eurofarma.euroforma.curso.CursoRepository;
import com.eurofarma.euroforma.educando.dto.CadastroEducandoRequest;
import com.eurofarma.euroforma.educando.dto.EducandoPerfilDto;
import com.eurofarma.euroforma.storage.StorageService;
import com.eurofarma.euroforma.usuario.Usuario;
import com.eurofarma.euroforma.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducandoServiceTest {

    @Mock
    private EducandoRepository educandoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CursoRepository cursoRepository;
    @Mock
    private AuditoriaRepository auditoriaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthService authService;
    @Mock
    private StorageService storageService;

    @InjectMocks
    private EducandoService educandoService;

    private Educando educandoExistente;

    @BeforeEach
    void setUp() {
        Usuario usuario = Usuario.builder().id(10L).nome("Ana Carolina Silva").email("ana.silva@email.com").build();
        Curso curso = Curso.builder().id(1L).nome("Auxiliar de Farmácia").build();
        educandoExistente = Educando.builder()
                .id(1L)
                .usuario(usuario)
                .curso(curso)
                .status(StatusEducando.EM_CURSO)
                .frequencia(92)
                .build();
    }

    @Test
    void cadastrarDeveCriarUsuarioEEducandoEEnviarConvite() {
        Curso curso = Curso.builder().id(2L).nome("Logística Farmacêutica").build();
        CadastroEducandoRequest request = new CadastroEducandoRequest(
                "Novo Educando", null, null, "novo@email.com", 2L,
                null, null, null, null, null, null, null, null, null, null, null);

        when(usuarioRepository.existsByEmailIgnoreCase("novo@email.com")).thenReturn(false);
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));
        when(passwordEncoder.encode(anyString())).thenReturn("hash-fake");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(99L);
            return u;
        });
        when(educandoRepository.save(any(Educando.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EducandoPerfilDto resultado = educandoService.cadastrar(request);

        assertThat(resultado.nome()).isEqualTo("Novo Educando");
        assertThat(resultado.status()).isEqualTo(StatusEducando.INSCRITO);
        assertThat(resultado.historico()).hasSize(1);
        verify(authService).enviarConviteDefinicaoSenha(any(Usuario.class));
    }

    @Test
    void atualizarStatusDeveRegistrarAuditoriaComValorAnteriorENovo() {
        when(educandoRepository.findById(1L)).thenReturn(Optional.of(educandoExistente));
        when(educandoRepository.save(any(Educando.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Usuario autor = Usuario.builder().id(5L).nome("Profa. Mariana Alves").build();
        when(usuarioRepository.getReferenceById(5L)).thenReturn(autor);

        educandoService.atualizarStatus(1L, StatusEducando.DESISTENTE, "Conflito de horário", 5L);

        assertThat(educandoExistente.getStatus()).isEqualTo(StatusEducando.DESISTENTE);
        assertThat(educandoExistente.getMotivoDesistencia()).isEqualTo("Conflito de horário");

        ArgumentCaptor<AuditoriaAlteracao> captor = ArgumentCaptor.forClass(AuditoriaAlteracao.class);
        verify(auditoriaRepository).save(captor.capture());
        AuditoriaAlteracao auditoria = captor.getValue();
        assertThat(auditoria.getCampo()).isEqualTo(CampoAuditoria.STATUS);
        assertThat(auditoria.getValorAnterior()).isEqualTo("EM_CURSO");
        assertThat(auditoria.getValorNovo()).isEqualTo("DESISTENTE");
        assertThat(auditoria.getAutor()).isEqualTo(autor);
    }

    @Test
    void atualizarFrequenciaLimpaMotivoApenasQuandoStatusMudaParaDesistente() {
        educandoExistente.setStatus(StatusEducando.CONCLUIDO);
        when(educandoRepository.findById(1L)).thenReturn(Optional.of(educandoExistente));
        when(educandoRepository.save(any(Educando.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioRepository.getReferenceById(5L)).thenReturn(Usuario.builder().id(5L).build());

        educandoService.atualizarFrequencia(1L, 100, 5L);

        assertThat(educandoExistente.getFrequencia()).isEqualTo(100);
        assertThat(educandoExistente.getStatus()).isEqualTo(StatusEducando.CONCLUIDO);
        verify(auditoriaRepository).save(any(AuditoriaAlteracao.class));
    }
}
