package com.mission.tablereservation.partner.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreUpdateForm {

    private String name;
    private String location;
    private String description;

}
