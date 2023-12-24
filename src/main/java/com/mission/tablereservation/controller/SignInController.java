package com.mission.tablereservation.controller;

import com.mission.tablereservation.config.JwtTokenProvider;
import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.model.CustomerDto;
import com.mission.tablereservation.model.SignInForm;
import com.mission.tablereservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signIn")
public class SignInController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/customer")
    public ResponseEntity<String> signIn(@RequestBody SignInForm signInForm){
        CustomerDto customerDto = this.memberService.authenticate(signInForm.getEmail(), signInForm.getPassword());
        List<UserType> roles = memberService.countAuthorities(customerDto);

        String token = this.jwtTokenProvider.createToken(customerDto, roles);
        return ResponseEntity.ok(token);
    }
}
