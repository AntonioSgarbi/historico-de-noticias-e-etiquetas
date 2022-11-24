package tech.antoniosgarbi.desafioapi.exception.details;

public class ValidationExceptionDetails extends ExceptionDetails {

    public ValidationExceptionDetails(String details) {
        super(
                "Error in fields validation",
                400,
                details,
                "verifique os modelos disponívels no caminho: '/swagger-ui/index.html#'"
        );
    }
}
