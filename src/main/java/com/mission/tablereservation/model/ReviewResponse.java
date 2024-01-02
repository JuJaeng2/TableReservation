package com.mission.tablereservation.model;

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

    public static ReviewResponse toResponse(Review review){
        return ReviewResponse.builder()
                .title(review.getTitle())
                .contents(review.getContents())
                .build();
    }

}
