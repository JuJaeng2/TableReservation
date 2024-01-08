package com.mission.tablereservation.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.mission.tablereservation.common.model.ResponseResultType.FAIL;
import static com.mission.tablereservation.common.model.ResponseResultType.SUCCESS;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    private ResponseMessageHeader header;
    private Object body;

    public static Object fail(String message, Object object){
        return ResponseMessage.builder()
                .header(ResponseMessageHeader.builder()
                        .message(message)
                        .resultType(FAIL)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build())
                .body(object)
                .build();
    }

    public static Object fail(String message){
        return fail(message, null);
    }

    public static Object success(Object object){
        return ResponseMessage.builder()
                .header(ResponseMessageHeader.builder()
                        .message("Response Success")
                        .resultType(SUCCESS)
                        .status(HttpStatus.OK.value())
                        .build())
                .body(object)
                .build();
    }

    public static Object success(){
        return fail(null);
    }
}
