package com.mission.tablereservation.controller;

import com.mission.tablereservation.common.model.MessageResponse;
import com.mission.tablereservation.common.model.ResponseResult;
import com.mission.tablereservation.customer.model.PartnerForm;
import com.mission.tablereservation.customer.model.SignUpForm;
import com.mission.tablereservation.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입
 * - 기본적으로 customer 권한을 가지고 회원가입한다.
 * - 로그인 후 partner 권한을 갖기 위해 partner 등록을 한다.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;
    /**
     * customer 회원가입
     */
    @PostMapping("/customer")
    public ResponseEntity<?> customerSignUp(@RequestBody SignUpForm form){

        MessageResponse response = signUpService.signup(form);
        return ResponseResult.success(response);
    }

    /**
     * partner 등록
     * - customer로 회원가입한 유저에게 partner 권한을 추가로 주는 것
     * - 이미 회원가입이 되어있는 유저만 partner 등록 가능
     */
    @PostMapping("/partner")
    public ResponseEntity<?> registerPartner(@RequestBody PartnerForm form, Authentication authentication){

        MessageResponse response = signUpService.registerPartner(form, authentication);

        return ResponseResult.success(response);
    }

}
