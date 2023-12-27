package com.mission.tablereservation.partner.controller;

import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.mission.tablereservation.exception.ErrorCode.NEED_LOGIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController {

    private final PartnerService partnerService;


    @PutMapping("/register")
    public ResponseEntity<StoreResponse> partnerStore(@RequestBody StoreForm form, Authentication authentication){

        if (authentication == null){
            throw new CustomException(NEED_LOGIN);
        }

        authentication.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority().equals("CUSTOMER")));
        // 매장 등록
        StoreResponse storeResponse = partnerService.registerStore(form, authentication);

        return new ResponseEntity<>(storeResponse ,HttpStatus.OK);
    }
}
