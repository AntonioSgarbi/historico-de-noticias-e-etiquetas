package tech.antoniosgarbi.desafioapi.service.impl;

import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.service.GeneratorService;

import java.util.Random;


@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Override
    public String generate(byte lenght) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$ %&";

        StringBuilder sb = new StringBuilder(lenght);
        Random random = new Random();

        for (int i = 0; i < lenght; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

}
