package com.example.multichar.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class DefaultAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e){
        ExceptionResponse er = new ExceptionResponse(HttpStatus.NOT_FOUND,e.getMessage());
        return new ResponseEntity<>(er, er.getStatusCode());
    }
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityExistsException(EntityExistsException e){
        ExceptionResponse er = new ExceptionResponse(HttpStatus.CONFLICT, e.getMessage());
        return new ResponseEntity<>(er, er.getStatusCode());
    }
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ExceptionResponse> handleSecurityException(SecurityException e){
        ExceptionResponse er = new ExceptionResponse(HttpStatus.FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(er,er.getStatusCode());
    }
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ExceptionResponse> handleTokenException(TokenException e){
        ExceptionResponse er = new ExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(er,er.getStatusCode());
    }
    @ExceptionHandler(CharSheetGenerateException.class)
    public ResponseEntity<ExceptionResponse> handleCharSheetGenerateException(CharSheetGenerateException e){
        ExceptionResponse er = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(er,er.getStatusCode());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse>handleRuntimeException(RuntimeException e){
        ExceptionResponse er = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "undefined error: "+e.getMessage());
        return new ResponseEntity<>(er,er.getStatusCode());
    }


}

class ExceptionResponse{
    private HttpStatus statusCode;
    private String message;

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExceptionResponse(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
