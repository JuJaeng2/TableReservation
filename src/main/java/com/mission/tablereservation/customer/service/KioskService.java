package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.model.KioskResponse;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.repository.ReservationRepository;
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
    private final int CONFIRM_TIME = 10;


    public KioskResponse confirmVisit(String email){

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));

        Reservation reservation = findReservation(customer);

        if (isConfirmationTimeOver(reservation.getReservationDate())){
            throw new CustomException(EXCEED_CONFIRMATION_TIME);
        }

        reservation.setVisit(true);
        reservationRepository.save(reservation);

        return KioskResponse.builder()
                .message("방문확인이 되었습니다.")
                .build();
    }

    private Reservation findReservation(Customer customer){
        List<Reservation> reservationList = reservationRepository.findByCustomerAndVisitIsFalseOrderByReservationDateAsc(customer);

        if (reservationList.isEmpty()){
            throw new CustomException(NO_RESERVATION);
        }

        return reservationList.get(0);
    }

    private boolean isConfirmationTimeOver(LocalDateTime reservationTime){

        if (LocalDateTime.now().isAfter(reservationTime.minusMinutes(CONFIRM_TIME))){
            return true;
        }

        return false;
    }
}
