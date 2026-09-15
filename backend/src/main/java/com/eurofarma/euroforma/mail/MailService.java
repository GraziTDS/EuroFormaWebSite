package com.eurofarma.euroforma.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${euroforma.mail.remetente}")
    private String remetente;

    @Value("${euroforma.frontend.reset-senha-url}")
    private String resetSenhaUrl;

    public void enviarEmailRedefinicaoSenha(String destinatario, String nome, String token) {
        String link = resetSenhaUrl + "?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remetente);
        message.setTo(destinatario);
        message.setSubject("euroForma — Redefinição de senha");
        message.setText("""
                Olá, %s!

                Recebemos uma solicitação para redefinir sua senha no euroForma.
                Acesse o link abaixo para criar uma nova senha (válido por 1 hora):

                %s

                Se você não solicitou isso, ignore este e-mail.
                """.formatted(nome, link));

        mailSender.send(message);
    }

    public void enviarEmailBoasVindas(String destinatario, String nome, String token) {
        String link = resetSenhaUrl + "?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remetente);
        message.setTo(destinatario);
        message.setSubject("euroForma — Bem-vindo(a)! Defina sua senha de acesso");
        message.setText("""
                Olá, %s!

                Seu cadastro no euroForma (Projeto Educandos do Instituto Eurofarma) foi criado.
                Acesse o link abaixo para definir sua senha e começar a usar a plataforma (válido por 1 hora):

                %s
                """.formatted(nome, link));

        mailSender.send(message);
    }
}
