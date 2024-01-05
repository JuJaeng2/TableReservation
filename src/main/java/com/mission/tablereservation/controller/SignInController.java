package com.mission.tablereservation.controller;

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

    @PostMapping("/customer")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInForm signInForm){

        CustomerDto customerDto = memberService.authenticate(signInForm.getEmail(), signInForm.getPassword());
        List<UserType> roles = memberService.countAuthorities(customerDto);

        TokenResponse tokenResponse = jwtTokenProvider.createToken(customerDto, roles);

        return ResponseEntity.ok(tokenResponse);
    }
}
