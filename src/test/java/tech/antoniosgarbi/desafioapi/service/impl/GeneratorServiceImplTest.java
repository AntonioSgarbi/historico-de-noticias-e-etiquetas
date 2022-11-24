package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
@SpringBootTest
class GeneratorServiceImplTest {

    @InjectMocks
    private GeneratorServiceImpl underTest;

    @Test
    @DisplayName("Should return random String with sixteen characters")
    void generate() {
        Random random = new Random();
        for (int i = 0; i <100; i++) {
            int length = random.nextInt(5, 20);
            assert this.underTest.generate((byte) length).toString().length() == length;
        }
    }
}