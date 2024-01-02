package com.mission.tablereservation.controller;

import com.mission.tablereservation.service.KioskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kiosk")
public class KioskController {

    private final KioskService kioskService;

    @PostMapping("/confirmation")
    public ResponseEntity<?> visitConfirmation(Authentication authentication) {

        String email = authentication.getName();

        String result = kioskService.confirmVisit(email);

        return ResponseEntity.ok(result);
    }
}
