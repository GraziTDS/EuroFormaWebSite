package com.eurofarma.euroforma.auth;

import com.eurofarma.euroforma.auth.dto.LoginRequest;
import com.eurofarma.euroforma.auth.dto.LoginResponse;
import com.eurofarma.euroforma.common.ApiException;
import com.eurofarma.euroforma.mail.MailService;
import com.eurofarma.euroforma.security.CustomUserDetails;
import com.eurofarma.euroforma.security.JwtService;
import com.eurofarma.euroforma.usuario.Usuario;
import com.eurofarma.euroforma.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public LoginResponse login(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var authentication = authenticationManager.authenticate(authToken);
        var userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.gerarToken(userDetails);
        return new LoginResponse(token, userDetails.getId(), userDetails.getNome(), userDetails.getEmail(), userDetails.getRole());
    }

    @Transactional
    public void esqueciSenha(String email) {
        usuarioRepository.findByEmailIgnoreCase(email).ifPresentOrElse(usuario -> {
            String token = criarTokenRedefinicao(usuario);
            mailService.enviarEmailRedefinicaoSenha(usuario.getEmail(), usuario.getNome(), token);
        }, () -> log.info("Solicitação de redefinição de senha para e-mail não cadastrado: {}", email));
    }

    @Transactional
    public void enviarConviteDefinicaoSenha(Usuario usuario) {
        String token = criarTokenRedefinicao(usuario);
        mailService.enviarEmailBoasVindas(usuario.getEmail(), usuario.getNome(), token);
    }

    private String criarTokenRedefinicao(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .usuario(usuario)
                .token(token)
                .expiraEm(Instant.now().plus(1, ChronoUnit.HOURS))
                .usado(false)
                .build();
        passwordResetTokenRepository.save(resetToken);
        return token;
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> ApiException.requisicaoInvalida("Token inválido"));

        if (resetToken.isUsado()) {
            throw ApiException.requisicaoInvalida("Este link de redefinição já foi utilizado");
        }
        if (resetToken.getExpiraEm().isBefore(Instant.now())) {
            throw ApiException.requisicaoInvalida("Este link de redefinição expirou");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        resetToken.setUsado(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
