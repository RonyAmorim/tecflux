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
     * Método para enviar email.
     * @param to Destinatário
     * @param subject Assunto
     * @param templateName Nome do template
     * @param context Contexto
     */
    public void sendEmail(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setFrom("noreply.tecflux@gmail.com"); // Campo 'from' configurado

            String content = templateEngine.process(templateName, context);
            messageHelper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar email", e);
        } catch (TemplateInputException e) {
            // Logar o erro e lidar com ele apropriadamente
            System.err.println("Template não encontrado: " + templateName);
            e.printStackTrace();
            throw new RuntimeException("Template não encontrado", e);
        }
    }
}
