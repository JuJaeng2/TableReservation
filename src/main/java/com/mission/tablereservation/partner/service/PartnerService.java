package com.mission.tablereservation.partner.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.customer.repository.ReservationRepository;
import com.mission.tablereservation.entity.Review;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.model.StoreUpdateForm;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.mission.tablereservation.customer.model.ReservationType.APPROVAL;
import static com.mission.tablereservation.customer.model.ReservationType.REFUSED;
import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

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
            throw new CustomException(NOT_OWNER_OF_STORE);
        }
        reviewRepository.delete(review);
        return "매장 관리자 권한으로 리뷰를 삭제했습니다.";
    }

    public String deleteStore(Long id, String email) {

        Store store = getStore(id);

        checkReservation(email, store);
        checkEmail(email, store);

        storeRepository.delete(store);

        return "매장을 삭제했습니다";
    }


    public StoreResponse updateStoreInfo(Long id, String email, StoreUpdateForm storeUpdateForm) {
        Store store = getStore(id);
        checkEmail(email, store);

        updateName(store, storeUpdateForm.getName());
        updateLocation(store, storeUpdateForm.getLocation());
        updateDescription(store, storeUpdateForm.getDescription());

        storeRepository.save(store);

        return StoreResponse.toResponse(store);
    }

    public String approveReservation(Long id, String email) {

        Reservation reservation = getReservation(id);

        checkValidity(email, reservation);

        reservation.setReservationType(APPROVAL);
        reservationRepository.save(reservation);

        return "예약승인을 완료했습니다.";

    }

    public String refuseReservation(Long id, String email) {

        Reservation reservation = getReservation(id);

        checkValidity(email, reservation);

        reservation.setReservationType(REFUSED);
        return "예약을 거절하였습니다.";
    }

    private static void checkValidity(String email, Reservation reservation) {
        if (!reservation.getStore().getCustomer().getEmail().equals(email)){
            throw new CustomException(NOT_OWNER_OF_STORE);
        }

        LocalDateTime curTime = LocalDateTime.now();

        if (curTime.isAfter(reservation.getReservationDate())){
            throw new CustomException(OVER_RESERVATION_DATE);
        }
    }

    private Reservation getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(NO_RESERVATION));
        return reservation;
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

    private Store getStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));
        return store;
    }

    private void checkEmail(String email, Store store) {
        if (!store.getCustomer().getEmail().equals(email)){
            throw  new CustomException(NOT_MATCH_EMAIL);
        }
    }
    private void checkReservation(String email, Store store){
        if (reservationRepository.countByStore(store) > 0){
            throw new CustomException(DELETE_ASSOCIATED_RESERVATION);
        }
    }



}
