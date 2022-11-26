package tech.antoniosgarbi.desafioapi.exception.details;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public abstract class ExceptionDetails {
    protected String title;
    protected int status;
    protected String details;
    protected String developerMessage;
    protected LocalDateTime timestamp;

    protected ExceptionDetails(String title, int status, String details,
                            String developerMessage) {
        this.title = title;
        this.status = status;
        this.details = details;
        this.developerMessage = developerMessage;
        timestamp = LocalDateTime.now();
    }

}
