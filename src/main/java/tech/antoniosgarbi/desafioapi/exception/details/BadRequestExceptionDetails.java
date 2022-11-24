package tech.antoniosgarbi.desafioapi.exception.details;

public class BadRequestExceptionDetails extends ExceptionDetails {
    public BadRequestExceptionDetails(String details) {
        super(
                "Bad Request Exception",
                400,
                details,
                "verifique as funções disponívels no caminho: '/swagger-ui/index.html#'"
        );
    }
}