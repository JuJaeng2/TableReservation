package com.mission.tablereservation.controller;

import com.mission.tablereservation.model.*;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.service.CustomerService;
import com.mission.tablereservation.service.ReservationService;
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

    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponse> storeReservation(@RequestBody ReservationForm reservationForm,
                                                                Authentication authentication){

        ReservationResponse reservationResponse  = reservationService.reserveStore(reservationForm, authentication.getName());

        return new ResponseEntity<>(reservationResponse, HttpStatus.OK);
    }

    @GetMapping("/search/store/{name}")
    public ResponseEntity<StoreResponse> searchStore(@PathVariable String name){
        StoreResponse storeResponse = storeService.getStoreInfo(name);
        return ResponseEntity.ok(storeResponse);
    }


    // 리뷰 작성
    /*
    - 예약전에는 리뷰작성 불가능하도록 하기
    -
     */
    @PostMapping("/review/{id}")
    public ResponseEntity<String> writeReview(@PathVariable(name = "id") Long reservationId,
                                              @RequestBody ReviewForm reviewForm,
                                              Authentication authentication){

        String result = customerService.writeReview(reviewForm, authentication.getName(), reservationId);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id, Authentication authentication){

        String email = authentication.getName();

        String result = customerService.deleteReview(id, email);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody ReviewUpdateForm reviewUpdateForm){

        String email = authentication.getName();

        ReviewResponse reviewResponse = customerService.updateReview(id, email, reviewUpdateForm);

        return ResponseEntity.ok(reviewResponse);
    }
}
