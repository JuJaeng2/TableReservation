package com.mission.tablereservation.partner.controller;

import com.mission.tablereservation.common.model.ResponseResult;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.common.model.MessageResponse;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.model.StoreUpdateForm;
import com.mission.tablereservation.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.mission.tablereservation.exception.ErrorCode.NEED_LOGIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerController {

    private final PartnerService partnerService;

    /**
     * 매장 등록
     * - 파트너가입이 된 계정만 매장 등록 가능
     */
    @PostMapping("/store")
    public ResponseEntity<?> registerStore(@RequestBody StoreForm form, Authentication authentication) {

        if (authentication == null) {
            throw new CustomException(NEED_LOGIN);
        }

        StoreResponse storeResponse = partnerService.registerStore(form, authentication);

        return ResponseResult.success(storeResponse);
    }

    /**
     * 매장 삭제
     * - 등록된 매장 삭제
     * - 매장에대한 예약이 있는 경우 매장정보 삭제 불가능
     * - 관련 예약을 모두 지워야 삭제 가능
     */
    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable(name = "storeId") Long id, Authentication authentication) {

        System.out.println("가게 아이디 : " + id);

        String email = authentication.getName();

        MessageResponse response = partnerService.deleteStore(id, email);

        return ResponseResult.success(response);
    }

    /**
     * 매장정보 수정
     * - 매장 정보 수정
     */
    @PutMapping("/store/{storeId}")
    public ResponseEntity<?> updateStore(@PathVariable(name = "storeId") Long id,
                                         @RequestBody StoreUpdateForm storeUpdateForm,
                                         Authentication authentication) {
        String email = authentication.getName();

        StoreResponse storeResponse = partnerService.updateStoreInfo(id, email, storeUpdateForm);

        return ResponseResult.success(storeResponse);
    }

    /**
     * 리뷰 삭제
     * - 자신이 등록한 매장에대한 리뷰 삭제 기능
     */
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "reviewId") Long id, Authentication authentication) {

        String email = authentication.getName();

        MessageResponse response = partnerService.deleteReview(id, email);

        return ResponseResult.success(response);
    }

    /**
     * 예약 승인
     */
    @PutMapping("/reservation/approval/{reservationId}")
    public ResponseEntity<?> approveReservation(@PathVariable(name = "reservationId") Long id, Authentication authentication){
        String email = authentication.getName();

        MessageResponse response = partnerService.approveReservation(id, email);

        return ResponseEntity.ok(response);
    }

    /**
     * 예약 거절
     */
    @PutMapping("/reservation/refusion/{reservationId}")
    public ResponseEntity<?> refuseReservation(@PathVariable(name = "reservationId") Long id, Authentication authentication) {
        String email = authentication.getName();

        MessageResponse response = partnerService.refuseReservation(id, email);

        return ResponseEntity.ok(response);
    }
}
