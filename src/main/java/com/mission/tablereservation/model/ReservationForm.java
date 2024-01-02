package com.mission.tablereservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationForm {

    private String storeName;

    private int numOfPeople;

    private LocalDateTime reservationDate;

}