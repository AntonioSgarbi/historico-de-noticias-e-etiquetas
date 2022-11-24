package tech.antoniosgarbi.desafioapi.exception;

public class NewsNotFoundException extends AbstractException {
    public NewsNotFoundException(String message) {
        super(message);
    }
}
