package com.mission.tablereservation.partner.service;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.entity.Review;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.exception.ErrorCode;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.CustomerRepository;
import com.mission.tablereservation.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mission.tablereservation.exception.ErrorCode.NOT_FOUND_PARTNER;
import static com.mission.tablereservation.exception.ErrorCode.NOT_FOUND_REVIEW;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public StoreResponse registerStore(StoreForm storeForm, Authentication authentication){

        String email = authentication.getName();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PARTNER));

        Store store = storeRepository.save(Store.createStore(storeForm, customer));

        return StoreResponse.toResponse(store);
    }

    public String deleteReview(Long reviewId, String email){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_REVIEW));

        if (!review.getReservation().getStore().getCustomer().getEmail().equals(email)){
            throw new CustomException(ErrorCode.NOT_STORE_BOSS);
        }
        reviewRepository.delete(review);
        return "매장 관리자 권한으로 리뷰를 삭제했습니다.";
    }
}
