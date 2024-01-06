package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.model.KioskResponse;
import com.mission.tablereservation.customer.model.ReservationType;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.customer.repository.ReservationRepository;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.exception.ErrorCode;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    
    private final int CONFIRM_TIME = 10;


    public KioskResponse confirmVisit(Long id, String email){

        Customer customer = getCustomer(email);
        Store store = getStore(id);
        Reservation reservation = checkReservation(customer, store);

        checkApproval(reservation);
        checkConfirmationTime(reservation);

        reservation.setVisit(true);
        reservationRepository.save(reservation);

        return KioskResponse.builder()
                .message("방문확인이 되었습니다.")
                .build();
    }

    private void checkApproval(Reservation reservation) {

        if (reservation.getReservationType() != ReservationType.APPROVAL){
            throw new CustomException(NOT_APPROVED_RESERVATION);
        }

    }

    private Customer getCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));
        return customer;
    }

    private Store getStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));
        return store;
    }

    private Reservation checkReservation(Customer customer, Store store) {
        Reservation reservation = reservationRepository.findByCustomerAndStore(customer, store)
                .orElseThrow(() -> new CustomException(NO_RESERVATION));

        if (reservation.getReservationType() != ReservationType.APPROVAL){
            throw new CustomException(ErrorCode.NOT_APPROVED_RESERVATION);
        }

        return reservation;
    }

    private void checkConfirmationTime(Reservation reservation) {
        if (LocalDateTime.now().isAfter(reservation.getReservationDate().minusMinutes(CONFIRM_TIME))){
            throw new CustomException(EXCEED_CONFIRMATION_TIME);
        }
    }

    private Reservation findReservation(Customer customer){
        List<Reservation> reservationList = reservationRepository.findByCustomerAndVisitIsFalseOrderByReservationDateAsc(customer);

        if (reservationList.isEmpty()){
            throw new CustomException(NO_RESERVATION);
        }

        return reservationList.get(0);
    }
}
