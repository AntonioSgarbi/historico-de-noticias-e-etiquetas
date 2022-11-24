package tech.antoniosgarbi.desafioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class AbstractException extends RuntimeException {
    private final LocalDateTime moment;

    public AbstractException(String message) {
        super(message);
        this.moment = LocalDateTime.now();
    }

}

