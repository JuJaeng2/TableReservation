package com.mission.tablereservation.customer.entity;

import com.mission.tablereservation.customer.model.ReservationForm;
import com.mission.tablereservation.customer.model.ReservationType;
import com.mission.tablereservation.partner.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    @ManyToOne
    @JoinColumn
    private Store store;

    private int numOfPeople;

    private LocalDateTime reservationDate;

    private boolean visit;

    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    private LocalDateTime regDate;

    public static Reservation toEntity(ReservationForm reservationForm, Customer customer, Store store){
        return Reservation.builder()
                .customer(customer)
                .store(store)
                .numOfPeople(reservationForm.getNumOfPeople())
                .reservationType(ReservationType.WAIT)
                .visit(false)
                .regDate(LocalDateTime.now())
                .build();
    }
}
