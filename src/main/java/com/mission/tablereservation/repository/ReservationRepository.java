package com.mission.tablereservation.repository;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.partner.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    int countByCustomerAndNumOfPeopleAndStore(Customer customer, int numOfPeople, Store store);

    int countByCustomerAndStoreAndRegDate(Customer customer,  Store store, LocalDateTime regDate);

    List<Reservation> findByCustomerAndVisitIsFalseOrderByReservationDateAsc(Customer customer);

    Optional<Reservation> findByCustomerAndStoreAndVisitIsTrue(Customer customer, Store store);

    long countByStore(Store store);
}
