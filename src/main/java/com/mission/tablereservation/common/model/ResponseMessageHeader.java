package com.mission.tablereservation.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessageHeader {

    private ResponseResultType resultType;
    private String resultCode;
    private String message;
    private int status;

}
