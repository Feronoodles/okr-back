package com.example.okr.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoExceptionValidation {
    String campo;
    String error;

    public DtoExceptionValidation(FieldError error) {
        this( error.getField(), error.getDefaultMessage());
    }
}
