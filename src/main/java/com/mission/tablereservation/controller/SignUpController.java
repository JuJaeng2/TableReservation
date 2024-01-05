package com.mission.tablereservation.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/customer")
    public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form){

        String result = signUpService.signup(form);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/partner")
    public ResponseEntity<String> registerPartner(@RequestBody PartnerForm form, Authentication authentication){

        String result = signUpService.registerPartner(form, authentication);

        return ResponseEntity.ok(result);
    }

}
