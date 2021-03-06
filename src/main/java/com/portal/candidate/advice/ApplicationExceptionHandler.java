package com.portal.candidate.advice;

import com.portal.candidate.exception.CandidateNotFoundException;
import com.portal.candidate.exception.SaveFailedException;
import com.portal.candidate.exception.UpdationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex)
    {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CandidateNotFoundException.class)
    public Map<String, String> handleIdException(CandidateNotFoundException ex)
    {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("ErrorMessage", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UpdationFailedException.class)
    public Map<String, String> handleUpdationFailedException(UpdationFailedException ex)
    {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("ErrorMessage ", ex.getMessage());
        return errorMap;
    }

    public Map<String, String> handleSaveFailedException(SaveFailedException ex)
    {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("ErrorMessage ", ex.getMessage());
        return errorMap;
    }
}
