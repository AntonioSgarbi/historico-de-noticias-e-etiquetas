package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class MailSpringServiceImpl {

    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender emailSender;


    public void sendText(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();

        this.prepare(message, to, subject, body);

        emailSender.send(message);
    }

    private void prepare(MimeMessage message, String to, String subject, String body) {
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(body, true);
        } catch (Exception e) { }

    }

}
