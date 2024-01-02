package com.mission.tablereservation.service;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.entity.Reservation;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.repository.CustomerRepository;
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

    private final int CONFIRMATION_TERM = 10;

    /**
     * 방문확인 기간이 지난 예약건은 어떻게 처리할지 => 삭제처리 후 이미 지난 예약건이라는 메세지 반환
     * 해당 매장의 파트너가입 유저가 확인할 수 있도록 따로 테이블을 만들기
     * 방문확인한 예약건은 파트너의 CheckReservationEntity로 넘기기..?
     * 아니면 에초에 예약을 접수하면 매장 파트너가입된 유저가 확인할 수 있도록 엔티티 만들기?
     * 아니면 새로운 항목을 만들어서 파트너가입된 유저가 에약을 취소했는지 허가했는지 boolean으로 표시하기?1
     */


    public String confirmVisit(String email){

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));

        List<Reservation> reservationList = reservationRepository.findByCustomerAndVisitIsFalseOrderByReservationDateAsc(customer);

        if (reservationList.isEmpty()){
            throw new CustomException(NO_RESERVATION);
        }

        Reservation reservation = reservationList.get(0);
        this.validateConfirmTime(reservation.getReservationDate());

        if (this.validateConfirmTime(reservation.getReservationDate())){
            this.deleteReservation(reservation);
            throw new CustomException(EXCEED_CONFIRMATION_TIME);
        }

        reservation.setVisit(true);
        reservationRepository.save(reservation);

        return "방문확인 성공";
    }

    private boolean validateConfirmTime(LocalDateTime reservationTime){
        if (!LocalDateTime.now().isBefore(reservationTime.minusMinutes(CONFIRMATION_TERM))){
            return false;
        }
        return true;
    }

    private void deleteReservation(Reservation reservation){
        reservationRepository.delete(reservation);
    }

}