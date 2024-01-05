package com.mission.tablereservation.customer.model;

import com.mission.tablereservation.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private String title;
    private String contents;
    private String message;

    public static ReviewResponse toResponse(String message){
        return toResponse(new Review(), message);
    }

    public static ReviewResponse toResponse(Review review, String message){
        return ReviewResponse.builder()
                .title(review.getTitle())
                .contents(review.getContents())
                .message(message)
                .build();
    }

}
