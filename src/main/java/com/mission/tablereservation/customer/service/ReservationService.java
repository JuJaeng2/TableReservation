package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.model.ReservationForm;
import com.mission.tablereservation.customer.model.ReservationResponse;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.customer.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    public ReservationResponse reserveStore(ReservationForm reservationForm, String email) {

        LocalDateTime reservationDate = createLocalDateTime(reservationForm);

        Customer customer = findCustomerByEmail(email);
        Store store = getStore(reservationForm);

        checkDuplicateReservation(reservationDate, customer, store);

        Reservation reservation = Reservation.toEntity(reservationForm, customer, store);
        reservation.setReservationDate(reservationDate);

        Reservation newReservation = reservationRepository.save(reservation);

        return ReservationResponse.toResponse(newReservation);
    }

    private LocalDateTime createLocalDateTime(ReservationForm reservationForm) {
        LocalDate date = reservationForm.getReservationDate();
        String time = reservationForm.getTime();

        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));

        return LocalDateTime.of(date, localTime);
    }

    private Store getStore(ReservationForm reservationForm) {
        Store store = storeRepository.findByName(reservationForm.getStoreName())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));
        return store;
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));
    }

    private void checkDuplicateReservation(LocalDateTime reservationDate, Customer customer, Store store) {
        List<Reservation> reservationList = reservationRepository.findAllByCustomerAndStore(customer, store);

        for (Reservation reservation : reservationList){
            if (reservation.getReservationDate().toLocalDate().equals(reservationDate.toLocalDate())){
                throw new CustomException(ALREADY_EXIST_RESERVATION);
            }
        }
    }

    @Transactional
    public void deleteReservation() {

        reservationRepository.deleteAllByReservationDateBefore(LocalDateTime.now());

    }
}
