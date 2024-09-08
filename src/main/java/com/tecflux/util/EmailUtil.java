package com.tecflux.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Classe utilitária para envio de emails.
 */
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * Método para enviar email genérico.
     * @param to Destinatário
     * @param subject Assunto
     * @param templateName Nome do template
     * @param context Contexto do email
     */
    public void sendEmail(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setFrom("noreply.tecflux@gmail.com");

            String content = templateEngine.process(templateName, context);
            messageHelper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar email", e);
        } catch (TemplateInputException e) {
            System.err.println("Template não encontrado: " + templateName);
            e.printStackTrace();
            throw new RuntimeException("Template não encontrado", e);
        }
    }

    /**
     * Método para enviar email de boas-vindas.
     * @param to Destinatário
     * @param nome Nome do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param linkPlataforma Link para acessar a plataforma
     */
    public void sendWelcomeEmail(String to, String nome, String email, String senha, String linkPlataforma) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject("Bem-vindo ao Tecflux!");
            messageHelper.setFrom("noreply.tecflux@gmail.com");

            // Criando o contexto para o Thymeleaf
            Context context = new Context();
            context.setVariable("nome", nome);
            context.setVariable("email", email);
            context.setVariable("senha", senha);
            context.setVariable("linkPlataforma", linkPlataforma);

            // Processando o template 'welcome-email'
            String content = templateEngine.process("Boas_Vindas", context);
            messageHelper.setText(content, true);

            // Enviando o email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar email de boas-vindas", e);
        }
    }
}
