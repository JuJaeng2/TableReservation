package com.mission.tablereservation.repository;

import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countByReservation(Reservation reservation);
}
