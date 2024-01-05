package com.mission.tablereservation.customer.controller;

import com.mission.tablereservation.customer.model.KioskResponse;
import com.mission.tablereservation.customer.service.KioskService;
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

    /**
     * 키오스크를 통한 방문확인
     * - 방문확인은 예약시간 10분전까지만 가능
     * - 방문확인 가능 시간을 지나면 방문확인 불가능
     */
    @PostMapping("/confirmation")
    public ResponseEntity<?> visitConfirmation(Authentication authentication) {

        String email = authentication.getName();

        KioskResponse kioskResponse = kioskService.confirmVisit(email);

        return ResponseEntity.ok(kioskResponse);
    }
}