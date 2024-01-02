package com.mission.tablereservation.entity;

import com.mission.tablereservation.model.ReviewForm;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    @OneToOne
    @JoinColumn
    private Reservation reservation;

    public static Review toEntity(ReviewForm reviewForm, Customer customer, Reservation reservation){
        return Review.builder()
                .title(reviewForm.getTitle())
                .contents(reviewForm.getContents())
                .customer(customer)
                .reservation(reservation)
                .build();
    }

}
