package tech.antoniosgarbi.desafioapi.exception.details;

public class RequestMethodNotSupportedExceptionDetails extends ExceptionDetails {
    public RequestMethodNotSupportedExceptionDetails(String details) {
        super(
                "Method not allowed Exception",
                405,
                "O caminho acessado não permite esse método",
                "verifique os métodos disponívels no caminho: '/swagger-ui/index.html#'"
        );
    }
}