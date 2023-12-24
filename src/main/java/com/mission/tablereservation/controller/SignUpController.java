package com.mission.tablereservation.controller;

import com.mission.tablereservation.model.PartnerForm;
import com.mission.tablereservation.model.SignUpForm;
import com.mission.tablereservation.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/customer")
    public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form){

        System.out.println(form.getName());
        String result = signUpService.signup(form);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/partner")
    public ResponseEntity<?> registerPartner(@RequestBody PartnerForm form, Authentication authentication){

        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("SecurityContextHolder에서 가져온 Authorities : " + authentication1.getAuthorities());

        System.out.println("토큰에 들어있는 이름 : " + authentication.getName());
        String result = signUpService.registerPartner(form, authentication);

        return ResponseEntity.ok(result);
    }

}
