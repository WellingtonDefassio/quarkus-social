package com.br.quarkus.social.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ResponseError {

    private String message;
    private Collection<FieldError> errors;

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations) {
        List<FieldError> fieldErrors = violations.stream().map(cv ->
                        new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
        String message = "Validation Error";

        return new ResponseError(message, fieldErrors);
    }

}
