package com.mission.tablereservation.partner.controller;

import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.model.StoreUpdateForm;
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


    @PostMapping("/store")
    public ResponseEntity<StoreResponse> registerStore(@RequestBody StoreForm form, Authentication authentication){

        if (authentication == null){
            throw new CustomException(NEED_LOGIN);
        }

        authentication.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority().equals("CUSTOMER")));
        // 매장 등록
        StoreResponse storeResponse = partnerService.registerStore(form, authentication);

        return new ResponseEntity<>(storeResponse ,HttpStatus.OK);
    }

    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable(name = "storeId") Long id, Authentication authentication){
        String email = authentication.getName();

        String result = partnerService.deleteStore(id, email);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/store/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable(name = "storeId") Long id,
                                         @RequestBody StoreUpdateForm storeUpdateForm,
                                         Authentication authentication){
        String email = authentication.getName();

        StoreResponse storeResponse = partnerService.updateStoreInfo(id, email, storeUpdateForm);

        return ResponseEntity.ok(storeResponse);
    }

    // 리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, Authentication authentication){

        String email = authentication.getName();

        String result = partnerService.deleteReview(reviewId, email);

        return ResponseEntity.ok(result);
    }

    /**
     * 자신의 매장과 관련된 예약 중에서 이미 방문확인 기간이 한참 초과된 예약을 지우는 기능을 통해 예약 리스트 정리하기
     */

}
