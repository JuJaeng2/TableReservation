package com.mission.tablereservation.common.model;

import org.springframework.http.ResponseEntity;

public class ResponseResult {

    public static ResponseEntity<?> fail(String message, Object object){
        return ResponseEntity.badRequest().body(ResponseMessage.fail(message, object));
    }

    public static ResponseEntity<?> fail(String message){

        return fail(message, null);
    }

    public static ResponseEntity<?> success(Object object){
        return ResponseEntity.ok().body(ResponseMessage.success(object));
    }

    public static ResponseEntity<?> success( ){
        return success(null);
    }


}
