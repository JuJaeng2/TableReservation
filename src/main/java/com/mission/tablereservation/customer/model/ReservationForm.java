package com.mission.tablereservation.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationForm {

    private String storeName;

    private int numOfPeople;

    private LocalDate reservationDate;

    private String time;

}
