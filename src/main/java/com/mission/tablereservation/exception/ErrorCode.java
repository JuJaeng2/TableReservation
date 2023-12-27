package com.mission.tablereservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    NOT_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    ALREADY_REGISTER_PARTNER(HttpStatus.BAD_REQUEST, "이미 파트너 가입이 되어있습니다."),
    NOT_REGISTER_PARTNER(HttpStatus.BAD_REQUEST, "파트너 가입이 되어있지 않습니다."),
    NOT_FOUND_PARTNER(HttpStatus.BAD_REQUEST, "해당 파트너를 찾을 수 없습니다."),
    NEED_LOGIN(HttpStatus.BAD_REQUEST, "로그인이 필요합니다."),
    UNMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "접근이 제한되었습니다.");


    private final HttpStatus httpStatus;
    private final String detail;
}
