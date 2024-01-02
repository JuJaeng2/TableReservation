package com.mission.tablereservation.service;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.entity.Reservation;
import com.mission.tablereservation.entity.Review;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.model.ReviewForm;
import com.mission.tablereservation.model.ReviewResponse;
import com.mission.tablereservation.model.ReviewUpdateForm;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.CustomerRepository;
import com.mission.tablereservation.repository.ReservationRepository;
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
    private final StoreRepository storeRepository;

    public String writeReview(ReviewForm reviewForm, String email, Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(NO_RESERVATION));

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CUSTOMER));

        if (reviewRepository.countByReservation(reservation) > 0){
            throw new CustomException(ALREADY_EXIST_REVIEW);
        };

        if (reservation.getCustomer() != customer){
            throw new CustomException(NOT_SAME_WRITER);
        }

        Review review = Review.toEntity(reviewForm, customer, reservation);

        reviewRepository.save(review);

        return "리뷰작성을 완료하였습니다.";
    }


    public String deleteReview(Long id, String email) {

        Review review = findReview(id, email);

        reviewRepository.delete(review);

        return "해당 리뷰를 삭제하였습니다.";
    }

    public ReviewResponse updateReview(Long id, String email, ReviewUpdateForm reviewUpdateForm) {
        Review review = findReview(id, email);

        System.out.println("리뷰 수정 제목 : " + reviewUpdateForm.getTitle());
        System.out.println("리뷰 수정 내용 : " + reviewUpdateForm.getContents());

        review.setTitle(reviewUpdateForm.getTitle());
        review.setContents(reviewUpdateForm.getContents());

        Review updateReview = reviewRepository.save(review);

        return ReviewResponse.toResponse(updateReview);

    }

    private Review findReview(Long id, String email) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_REVIEW));

        if (!this.isEmailSame(review, email)){
            throw new CustomException(NOT_MATCH_EMAIL);
        }
        return review;
    }

    private boolean isEmailSame(Review review, String email){
        return review.getCustomer().getEmail().equals(email);
    }
}
