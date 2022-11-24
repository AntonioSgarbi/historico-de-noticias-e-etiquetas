package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MailSpringServiceImplTest {

    @Mock
    private JavaMailSenderImpl javaMailSender;
    @InjectMocks
    private MailSpringServiceImpl underTest;

    @Test
    @DisplayName("Should send SimpleMailMessage when successFull")
    void SendText() {
        this.underTest.sendText("to", "subject", "body");


        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}