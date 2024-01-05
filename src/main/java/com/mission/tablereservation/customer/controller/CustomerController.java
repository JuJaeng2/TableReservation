package com.mission.tablereservation.customer.controller;

import com.mission.tablereservation.customer.model.*;
import com.mission.tablereservation.customer.service.CustomerService;
import com.mission.tablereservation.customer.service.ReservationService;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
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
    public ResponseEntity<ReservationResponse> storeReservation(@RequestBody ReservationForm reservationForm,
                                                                Authentication authentication) {

        ReservationResponse reservationResponse =
                reservationService.reserveStore(reservationForm, authentication.getName());

        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }

    /**
     * 매장검색
     * - 매장이름을 통해 검색
     * - 해당 매장 정보 제공
     */
    @GetMapping("/search/store/{storeName}")
    public ResponseEntity<StoreResponse> searchStore(@PathVariable(name = "storeName") String name) {
        StoreResponse storeResponse = storeService.getStoreInfo(name);
        return ResponseEntity.ok(storeResponse);
    }

    /**
     * 리뷰 작성
     * - 예약한 매장에 대한 리뷰만 작성 가능
     * - 이미 작성한 리뷰가 있으면 작성 불가능
     */
    @PostMapping("/review/{reservationId}")
    public ResponseEntity<ReviewResponse> writeReview(@PathVariable(name = "reservationId") Long id,
                                              @RequestBody ReviewForm reviewForm,
                                              Authentication authentication) {
        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.writeReview(reviewForm, email, id);

        return ResponseEntity.ok(reviewResponse);
    }
    /**
     * 리뷰 삭제
     * - 삭제하고자 하는 리뷰 아이디를 통해 리뷰 삭제
     * - 리뷰 삭제는 작성자, 매장 관리자만 삭제 가능
     */
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable(name = "reviewId") Long id, Authentication authentication) {

        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.deleteReview(id, email);

        return ResponseEntity.ok(reviewResponse);
    }
    /**
     * 리뷰 수정
     * - 자신이 작성한 리뷰 수정
     * - 리뷰 작성자만 수정 가능
     */
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable(name = "reviewId") Long id,
                                                       Authentication authentication,
                                                       @RequestBody ReviewUpdateForm reviewUpdateForm) {

        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.updateReview(id, email, reviewUpdateForm);

        return ResponseEntity.ok(reviewResponse);
    }
}
