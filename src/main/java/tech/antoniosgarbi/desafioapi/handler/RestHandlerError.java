package tech.antoniosgarbi.desafioapi.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.antoniosgarbi.desafioapi.exception.AbstractException;
import tech.antoniosgarbi.desafioapi.exception.details.BadRequestExceptionDetails;
import tech.antoniosgarbi.desafioapi.exception.details.NotFoundExceptionDetails;
import tech.antoniosgarbi.desafioapi.exception.details.RequestMethodNotSupportedExceptionDetails;
import tech.antoniosgarbi.desafioapi.exception.details.ValidationExceptionDetails;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestHandlerError extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationExceptionDetails> handleValidationException(ConstraintViolationException cve) {
        ValidationExceptionDetails details = new ValidationExceptionDetails(cve.getConstraintViolations().toString());
        return ResponseEntity.badRequest().body(details);
    }

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(AbstractException bre) {
        BadRequestExceptionDetails details = new BadRequestExceptionDetails(bre.getMessage());
        return ResponseEntity.badRequest().body(details);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RequestMethodNotSupportedExceptionDetails notSupportedExceptionDetails = new RequestMethodNotSupportedExceptionDetails();
        return handleExceptionInternal(ex, notSupportedExceptionDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        NotFoundExceptionDetails notFoundDetails = new NotFoundExceptionDetails();
        return handleExceptionInternal(ex, notFoundDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequestExceptionDetails badReqDetails = new BadRequestExceptionDetails(ex.getMessage());
        return handleExceptionInternal(ex, badReqDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequestExceptionDetails badReqDetails = new BadRequestExceptionDetails(ex.getMessage());
        return handleExceptionInternal(ex, badReqDetails, headers, status, request);
    }

}
