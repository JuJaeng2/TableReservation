package com.mission.tablereservation.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateForm {

    private String title;
    private String contents;

}
