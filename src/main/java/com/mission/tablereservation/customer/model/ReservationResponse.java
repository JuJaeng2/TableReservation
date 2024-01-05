package com.mission.tablereservation.customer.model;

import com.mission.tablereservation.customer.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReservationResponse {

    private Long reservationNum;
    private String storeName;
    private String customerName;
    private LocalDateTime reservationDate;

    public static ReservationResponse toResponse(Reservation reservation){
        return ReservationResponse.builder()
                .reservationNum(reservation.getId())
                .storeName(reservation.getStore().getName())
                .customerName(reservation.getCustomer().getName())
                .reservationDate(reservation.getReservationDate())
                .build();

    }
}
