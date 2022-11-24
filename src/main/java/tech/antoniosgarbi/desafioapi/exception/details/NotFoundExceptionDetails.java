package tech.antoniosgarbi.desafioapi.exception.details;

public class NotFoundExceptionDetails extends ExceptionDetails {
    public NotFoundExceptionDetails(String details) {
        super(
                "NotFound Exception",
                404,
                "O caminho acessado não existe",
                "verifique as rotas disponívels no caminho: '/swagger-ui/index.html#'"
        );
    }


}