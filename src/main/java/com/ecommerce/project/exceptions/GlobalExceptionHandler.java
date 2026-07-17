package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentException(MethodArgumentNotValidException e){
        Map<String,String> map=new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err ->{
            String fieldName= ((FieldError)err).getField();
            String message=err.getDefaultMessage();
            map.put(fieldName,message);
                }
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleResourceNotFoundException(ResourceNotFoundException e){
        String message=e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(message,"false"));
    }
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse>handleAPIException(APIException e){
        String message=e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse(message,"false"));
    }
}
