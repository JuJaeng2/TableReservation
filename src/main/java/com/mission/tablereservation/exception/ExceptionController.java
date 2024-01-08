package com.mission.tablereservation.exception;

import com.mission.tablereservation.common.model.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException exception){
        return ResponseResult.fail(exception.getMessage());
//        return new ResponseEntity<>(exception.getMessage(),exception.getErrorCode().getHttpStatus());
    }

}
