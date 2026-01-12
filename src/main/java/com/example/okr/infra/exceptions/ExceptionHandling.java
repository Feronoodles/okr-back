package com.example.okr.infra.exceptions;


import com.example.okr.dto.exception.DtoExceptionValidation;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity exception404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity exception400(MethodArgumentNotValidException e){
        var errores= e.getFieldErrors().stream().map(DtoExceptionValidation::new);
        return ResponseEntity.badRequest().body(errores);
    }
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity exceptionSort400(PropertyReferenceException e)
    {

        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity exceptionSort400(HttpRequestMethodNotSupportedException e)
    {

        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity exceptionNullPointer(NullPointerException e)
    {

        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity exceptionNotReadable(HttpMessageNotReadableException e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity exceptionNotReadable(InvalidDataAccessApiUsageException e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity exceptionIntegrity(SQLIntegrityConstraintViolationException e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
