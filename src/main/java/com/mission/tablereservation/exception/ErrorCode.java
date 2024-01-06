package com.mission.tablereservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    NOT_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),

    NOT_FOUND_CUSTOMER(HttpStatus.BAD_REQUEST, "사용자를 찾을수 없습니다."),
    ALREADY_REGISTER_PARTNER(HttpStatus.BAD_REQUEST, "이미 파트너 가입이 되어있습니다."),
    NOT_REGISTER_PARTNER(HttpStatus.BAD_REQUEST, "파트너 가입이 되어있지 않습니다."),
    NOT_FOUND_PARTNER(HttpStatus.BAD_REQUEST, "해당 파트너를 찾을 수 없습니다."),
    NEED_LOGIN(HttpStatus.BAD_REQUEST, "로그인이 필요합니다."),
    UNMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "접근이 제한되었습니다."),

    NOT_FOUND_STORE(HttpStatus.BAD_REQUEST, "존재하지 않는 매장입니다."),

    ALREADY_EXIST_RESERVATION(HttpStatus.BAD_REQUEST, "동일한 예약이 이미 존재합니다."),
    NO_RESERVATION(HttpStatus.BAD_REQUEST, "예약정보가 없습니다."),

    EXCEED_CONFIRMATION_TIME(HttpStatus.BAD_REQUEST, "방문확인 기간이 지났습니다. 사장님에게 문의하세요."),

    NOT_SAME_WRITER(HttpStatus.BAD_REQUEST, "예약자와 요청자가 동일하지 않습니다"),
    NOT_MATCH_EMAIL(HttpStatus.BAD_REQUEST, "리뷰작성자와 리뷰삭제 요청자가 다릅니다."),
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "존재하지않는 리뷰입니다."),
    ALREADY_EXIST_REVIEW(HttpStatus.BAD_REQUEST, "이미 리뷰가 존재합니다."),
    NOT_OWNER_OF_STORE(HttpStatus.BAD_REQUEST, "매장의 관리자만 리뷰를 삭제할 수 있습니다." ),
    NOT_CONFIRM_VISIT(HttpStatus.BAD_REQUEST, "아직 방문확인 처리가 되지 않아 리뷰를 작성할 수 없습니다. 방문확인을 먼저 해주세요."),
    DELETE_ASSOCIATED_RESERVATION(HttpStatus.BAD_REQUEST, "삭제하려는 매장과 관련된 모든 예약을 삭제해 주세요"),
    OVER_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "예약 기간이 지난 예약입니다. 기간이 지난 예약은 삭제해주세요."),
    NOT_APPROVED_RESERVATION(HttpStatus.BAD_REQUEST, "승인되지 않은 예약입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
