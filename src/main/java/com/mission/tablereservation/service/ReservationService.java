package com.mission.tablereservation.service;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.entity.Reservation;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.model.ReservationForm;
import com.mission.tablereservation.model.ReservationResponse;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mission.tablereservation.exception.ErrorCode.ALREADY_EXIST_RESERVATION;
import static com.mission.tablereservation.exception.ErrorCode.NOT_FOUND_STORE;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final StoreRepository storeRepository;

    @Transactional
    public ReservationResponse reserveStore(ReservationForm reservationForm, String email){

        Customer customer = memberService.findCustomerByEmail(email);
        Store store = storeRepository.findByName(reservationForm.getStoreName())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

        if (this.checkDuplicateReservation(reservationForm, customer, store)){
            throw new CustomException(ALREADY_EXIST_RESERVATION);
        }

        Reservation reservation = Reservation.toEntity(reservationForm, customer, store);

        Reservation newReservation = reservationRepository.save(reservation);

        return ReservationResponse.toResponse(newReservation);
    }

    private boolean checkDuplicateReservation(ReservationForm reservationForm, Customer customer, Store store){
        int cnt = reservationRepository.countByCustomerAndNumOfPeopleAndStore(customer, reservationForm.getNumOfPeople(), store);
        if (cnt > 0){
            return true;
        }
        return false;
    }
}
