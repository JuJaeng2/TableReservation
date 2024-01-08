package com.mission.tablereservation.controller;

import com.mission.tablereservation.common.model.ResponseResult;
import com.mission.tablereservation.config.JwtTokenProvider;
import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.customer.model.CustomerDto;
import com.mission.tablereservation.customer.model.SignInForm;
import com.mission.tablereservation.customer.model.TokenResponse;
import com.mission.tablereservation.customer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signIn")
public class SignInController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * customer 로그인
     * - 이메일과 패스워드를 통해 로그인
     * - 이메일과 패스워드가 적합하다면 토큰을 반환
     */
    @PostMapping("/customer")
    public ResponseEntity<?> signIn(@RequestBody SignInForm signInForm){

        CustomerDto customerDto = memberService.authenticate(signInForm.getEmail(), signInForm.getPassword());
        List<UserType> roles = memberService.countAuthorities(customerDto);

        TokenResponse tokenResponse = jwtTokenProvider.createToken(customerDto, roles);

        return ResponseResult.success(tokenResponse.getToken());
    }
}
