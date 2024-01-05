package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.model.ReservationForm;
import com.mission.tablereservation.customer.model.ReservationResponse;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    public ReservationResponse reserveStore(ReservationForm reservationForm, String email) {

        Customer customer = findCustomerByEmail(email);
        Store store = storeRepository.findByName(reservationForm.getStoreName())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

        checkDuplicateReservation(reservationForm, customer, store);

        Reservation reservation = Reservation.toEntity(reservationForm, customer, store);

        Reservation newReservation = reservationRepository.save(reservation);

        return ReservationResponse.toResponse(newReservation);
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));
    }

    private void checkDuplicateReservation(ReservationForm reservationForm, Customer customer, Store store) {
        int cnt = reservationRepository.countByCustomerAndStoreAndRegDate(customer, store, reservationForm.getReservationDate());
        if (cnt > 0) {
            throw new CustomException(ALREADY_EXIST_RESERVATION);
        }
    }
}
