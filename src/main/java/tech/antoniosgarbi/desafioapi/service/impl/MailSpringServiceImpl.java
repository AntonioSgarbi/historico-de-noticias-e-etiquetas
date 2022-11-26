package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.configuration.AuthenticationFilter;

import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class MailSpringServiceImpl {

    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender emailSender;
    private static final Logger loggerInstance = LoggerFactory.getLogger(MailSpringServiceImpl.class);


    public void sendText(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(body, true);
        } catch (Exception e) {
            loggerInstance.error("mail error on helper {}", e.getMessage());
        }
        try {
            emailSender.send(message);
        } catch (Exception e) {
            loggerInstance.error("mail error on send {}", e.getMessage());

        }

    }

}
