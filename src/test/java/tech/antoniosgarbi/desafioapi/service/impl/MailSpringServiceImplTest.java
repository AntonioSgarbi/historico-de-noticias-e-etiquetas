package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MailSpringServiceImplTest {

    @Mock
    private JavaMailSenderImpl javaMailSender;
    @Mock
    private MimeMessage mimeMessage;
    @InjectMocks
    private MailSpringServiceImpl underTest;

    @Test
    @DisplayName("Should send MimeMessage when successFull")
    void SendText1() {
        when(javaMailSender.createMimeMessage()).thenReturn(this.mimeMessage);

        this.underTest.sendText("to", "subject", "body");

        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Should not throw exception when assembling email")
    void SendText2() {
        when(javaMailSender.createMimeMessage()).thenReturn(this.mimeMessage);

        assertDoesNotThrow(() -> this.underTest.sendText("to", "subject", "body"));
    }


}