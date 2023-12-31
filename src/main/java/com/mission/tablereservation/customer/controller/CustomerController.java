package com.mission.tablereservation.customer.controller;

import com.mission.tablereservation.common.model.ResponseResult;
import com.mission.tablereservation.customer.model.*;
import com.mission.tablereservation.customer.service.CustomerService;
import com.mission.tablereservation.customer.service.ReservationService;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    private final ReservationService reservationService;
    private final StoreService storeService;
    private final CustomerService customerService;

    /**
     * 매장 예약
     * - 로그인한 클라이언트만 예약 가능
     * - 한 사람이 같은 날 동일한 매장 예약 불가능
     */
    @PostMapping("/reservation")
    public ResponseEntity<?> storeReservation(@RequestBody ReservationForm reservationForm,
                                                                Authentication authentication) {

        log.info("예약 희망 날짜 : " + reservationForm.getStoreName());
        System.out.println(reservationForm.getReservationDate());

        ReservationResponse reservationResponse =
                reservationService.reserveStore(reservationForm, authentication.getName());

        return ResponseResult.success(reservationResponse);
    }

    /**
     * 매장검색
     * - 매장이름을 통해 검색
     * - 해당 매장 정보 제공
     */
    @GetMapping("/search/store/{storeName}")
    public ResponseEntity<?> searchStore(@PathVariable(name = "storeName") String name) {
        StoreResponse storeResponse = storeService.getStoreInfo(name);
        return ResponseResult.success(storeResponse);
    }

    /**
     * 리뷰 작성
     * - 예약한 매장에 대한 리뷰만 작성 가능
     * - 이미 작성한 리뷰가 있으면 작성 불가능
     */
    @PostMapping("/review/{reservationId}")
    public ResponseEntity<?> writeReview(@PathVariable(name = "reservationId") Long id,
                                              @RequestBody ReviewForm reviewForm,
                                              Authentication authentication) {
        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.writeReview(reviewForm, email, id);

        return ResponseResult.success(reviewResponse);
    }

    /**
     * 리뷰 삭제
     * - 삭제하고자 하는 리뷰 아이디를 통해 리뷰 삭제
     * - 리뷰 삭제는 작성자, 매장 관리자만 삭제 가능
     */
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "reviewId") Long id, Authentication authentication) {

        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.deleteReview(id, email);

        return ResponseResult.success(reviewResponse);
    }

    /**
     * 리뷰 수정
     * - 자신이 작성한 리뷰 수정
     * - 리뷰 작성자만 수정 가능
     */
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable(name = "reviewId") Long id,
                                                       Authentication authentication,
                                                       @RequestBody ReviewUpdateForm reviewUpdateForm) {

        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.updateReview(id, email, reviewUpdateForm);

        return ResponseResult.success(reviewResponse);
    }
}
