package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.entity.Reservation;
import com.mission.tablereservation.customer.model.ReviewForm;
import com.mission.tablereservation.customer.model.ReviewResponse;
import com.mission.tablereservation.customer.model.ReviewUpdateForm;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.entity.Review;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.customer.repository.ReservationRepository;
import com.mission.tablereservation.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    public ReviewResponse writeReview(ReviewForm reviewForm, String email, Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(NO_RESERVATION));

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));

        checkCustomerMatch(reservation, customer);
        checkReviewExist(reservation);
        checkVisitConfirmation(reservation);

        Review review = Review.toEntity(reviewForm, customer, reservation);
        reviewRepository.save(review);

        return ReviewResponse.toResponse(review ,"리뷰작성을 왈료했습니다.");

    }

    public ReviewResponse deleteReview(Long id, String email) {

        Review review = findReview(id, email);

        reviewRepository.delete(review);

        return ReviewResponse.toResponse("해당 리뷰를 삭제하였습니다.");
    }

    public ReviewResponse updateReview(Long id, String email, ReviewUpdateForm reviewUpdateForm) {
        Review review = findReview(id, email);

        review.setTitle(reviewUpdateForm.getTitle());
        review.setContents(reviewUpdateForm.getContents());

        Review updateReview = reviewRepository.save(review);

        return ReviewResponse.toResponse(updateReview, "리뷰를 수정하였습니다.");

    }

    private void checkCustomerMatch(Reservation reservation, Customer customer){
        if (reservation.getCustomer() != customer){
            throw new CustomException(NOT_SAME_WRITER);
        }
    }

    private void checkReviewExist(Reservation reservation){
        if (reviewRepository.countByReservation(reservation) > 0){
            throw new CustomException(ALREADY_EXIST_REVIEW);
        }
    }

    private void checkVisitConfirmation(Reservation reservation){
        if (!reservation.isVisit()){
            throw new CustomException(NOT_CONFIRM_VISIT);
        }
    }

    private Review findReview(Long id, String email) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_REVIEW));

        checkEmailMatchesReviewer(review, email);

        return review;
    }

    private void checkEmailMatchesReviewer(Review review, String email){

        if (!review.getCustomer().getEmail().equals(email)){
            throw new CustomException(NOT_MATCH_EMAIL);
        }
    }
}
