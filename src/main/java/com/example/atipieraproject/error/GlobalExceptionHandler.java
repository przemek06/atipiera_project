package com.example.atipieraproject.error;

import com.example.atipieraproject.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({GithubUserNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleGithubUserNotFound(GithubUserNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO(404, ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({APIRateExceededException.class})
    public ResponseEntity<ErrorDTO> handleAPIRateExceeded(APIRateExceededException ex) {
        ErrorDTO errorDTO = new ErrorDTO(403, ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ServiceNotRespondingException.class})
    public ResponseEntity<ErrorDTO> handleServiceNotResponding(ServiceNotRespondingException ex) {
        ErrorDTO errorDTO = new ErrorDTO(503, ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        ErrorDTO errorDTO = new ErrorDTO(406, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDTO);
    }
}
