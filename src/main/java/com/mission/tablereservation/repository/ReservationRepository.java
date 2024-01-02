package com.mission.tablereservation.repository;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.entity.Reservation;
import com.mission.tablereservation.partner.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    int countByCustomerAndNumOfPeopleAndStore(Customer customer, int numOfPeople, Store store);

    List<Reservation> findByCustomerAndVisitIsFalseOrderByReservationDateAsc(Customer customer);

    Optional<Reservation> findByCustomerAndStoreAndVisitIsTrue(Customer customer, Store store);
}
