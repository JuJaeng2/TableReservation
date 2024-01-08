package com.mission.tablereservation.partner.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.customer.repository.ReservationRepository;
import com.mission.tablereservation.entity.Review;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.common.model.MessageResponse;
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

    /**
     *  매장 등록
     *  - 한명의 파트너가 동일한 이름의 매장을 하나 이상 등록할 수 없다.
     */
    @Transactional
    public StoreResponse registerStore(StoreForm storeForm, Authentication authentication){

        String email = authentication.getName();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PARTNER));

        if (storeRepository.countByNameAndCustomer(storeForm.getName(), customer) > 0){
            throw new CustomException(ALREADY_EXIST_STORE);
        }

        Store store = storeRepository.save(Store.createStore(storeForm, customer));

        return StoreResponse.toResponse(store);
    }

    /**
     * 매장 삭제
     * - 삭제하려는 매장에대한 예약이 없는지 확인 -> 있다면 예약 삭제
     */
    @Transactional
    public MessageResponse deleteStore(Long id, String email) {

        Store store = getStore(id);

        checkReservation(email, store);
        checkEmail(email, store);

        storeRepository.delete(store);

        return MessageResponse.builder()
                .message("매장을 삭제했습니다")
                .build();
    }

    /**
     * 매장정보 수정
     */
    @Transactional
    public StoreResponse updateStoreInfo(Long id, String email, StoreUpdateForm storeUpdateForm) {
        Store store = getStore(id);
        checkEmail(email, store);

        updateName(store, storeUpdateForm.getName());
        updateLocation(store, storeUpdateForm.getLocation());
        updateDescription(store, storeUpdateForm.getDescription());

        storeRepository.save(store);

        return StoreResponse.toResponse(store);
    }


    /**
     *  리뷰 삭제
     *  - 파트너가 등록한 매장에 한하여 리뷰 삭제
     */
    @Transactional
    public MessageResponse deleteReview(Long reviewId, String email){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_REVIEW));

        if (!review.getReservation().getStore().getCustomer().getEmail().equals(email)){
            throw new CustomException(NOT_OWNER_OF_STORE);
        }
        reviewRepository.delete(review);

        return MessageResponse.builder()
                .message("매장 관리자 권한으로 리뷰를 삭제했습니다.")
                .build();
    }

    /**
     * 예약 승인
     * - 파트너가 등록한 매장에 대한 예약을 승인
     */
    public MessageResponse approveReservation(Long id, String email) {

        Reservation reservation = getReservation(id);

        checkValidity(email, reservation);

        reservation.setReservationType(APPROVAL);
        reservationRepository.save(reservation);

        return MessageResponse.builder()
                .message("예약승인을 완료했습니다.")
                .build();
    }

    /**
     * 예약 거절
     * - 파트너가 등록한 매장에 대한 예약을 거절
     */
    public MessageResponse refuseReservation(Long id, String email) {

        Reservation reservation = getReservation(id);

        checkValidity(email, reservation);

        reservation.setReservationType(REFUSED);
        return MessageResponse.builder()
                .message("예약을 거절하였습니다.")
                .build();
    }

    /**
     * 거절하려는 유저가 예약을 받은 매장의 파트너가 맞는지 확인
     */
    private static void checkValidity(String email, Reservation reservation) {
        if (!reservation.getStore().getCustomer().getEmail().equals(email)){
            throw new CustomException(NOT_OWNER_OF_STORE);
        }

        LocalDateTime curTime = LocalDateTime.now();

        if (curTime.isAfter(reservation.getReservationDate())){
            throw new CustomException(OVER_RESERVATION_DATE);
        }
    }

    /**
     * 예약정보를 가져옴
     */
    private Reservation getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(NO_RESERVATION));
        return reservation;
    }

    /**
     * Store 객체에 매장설명 수정
     */
    private void updateDescription(Store store, String newDescription) {
        if (newDescription != null && newDescription.equals(store.getDescription())){
            store.setDescription(newDescription);
        }
    }

    /**
     * Store 객체에 위치 정보 수정
     */
    private void updateLocation(Store store, String newLocation) {
        if (newLocation != null && !newLocation.equals(store.getLocation())){
            store.setLocation(newLocation);
        }
    }

    /**
     * Store 객체에 이름 수정
     */
    private void updateName(Store store, String newStoreName) {
        if (newStoreName != null && !newStoreName.equals(store.getName())){
            store.setName(newStoreName);
        }
    }

    /**
     * 매장객체 가져오기
     */
    private Store getStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));
        return store;
    }

    /**
     * 이메일을 통해 매장의 주인이 맞는지 확인
     */
    private void checkEmail(String email, Store store) {
        if (!store.getCustomer().getEmail().equals(email)){
            throw  new CustomException(NOT_MATCH_EMAIL);
        }
    }

    /**
     * 삭제하려는 매장에 예약이 있는지 확인
     * - 예약이 있다면 예외를 던진다(예약을 삭제하라는 문구)
     */
    private void checkReservation(String email, Store store){
        if (reservationRepository.countByStore(store) > 0){
            throw new CustomException(DELETE_ASSOCIATED_RESERVATION);
        }
    }



}
