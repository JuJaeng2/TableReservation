package com.mission.tablereservation.partner.service;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.entity.Review;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.model.StoreUpdateForm;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.CustomerRepository;
import com.mission.tablereservation.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mission.tablereservation.exception.ErrorCode.*;

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
            throw new CustomException(NOT_STORE_BOSS);
        }
        reviewRepository.delete(review);
        return "매장 관리자 권한으로 리뷰를 삭제했습니다.";
    }

    public String deleteStore(Long id, String email) {

        System.out.println("매장 ID : " + id);
        System.out.println("요청자 이메일 : " + email);
        Store store = findStore(id);

        checkEmail(email, store);

        storeRepository.delete(store);

        return "매장을 삭제했습니다";
    }


    public StoreResponse updateStoreInfo(Long id, String email, StoreUpdateForm storeUpdateForm) {
        Store store = findStore(id);
        checkEmail(email, store);

        updateName(store, storeUpdateForm.getName());
        updateLocation(store, storeUpdateForm.getLocation());
        updateDescription(store, storeUpdateForm.getDescription());

        storeRepository.save(store);

        return StoreResponse.toResponse(store);
    }

    private void updateDescription(Store store, String newDescription) {
        if (newDescription != null && newDescription.equals(store.getDescription())){
            store.setDescription(newDescription);
        }
    }

    private void updateLocation(Store store, String newLocation) {
        if (newLocation != null && !newLocation.equals(store.getLocation())){
            store.setLocation(newLocation);
        }
    }

    private void updateName(Store store, String newStoreName) {
        if (newStoreName != null && !newStoreName.equals(store.getName())){
            store.setName(newStoreName);
        }
    }

    private Store findStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_EXIST_STORE));
        return store;
    }

    private static void checkEmail(String email, Store store) {
        if (!store.getCustomer().getEmail().equals(email)){
            throw  new CustomException(NOT_MATCH_EMAIL);
        }
    }
}
