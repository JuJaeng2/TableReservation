package com.mission.tablereservation.partner.model;

import com.mission.tablereservation.partner.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreResponse {

    private long id;
    private String name;
    private String location;
    private String description;

    private long partnerId;
    private String partnerName;

    public static StoreResponse toResponse(Store store) {

        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .location(store.getLocation())
                .description(store.getDescription())
                .partnerId(store.getCustomer().getId())
                .partnerName(store.getCustomer().getName())
                .build();

    }
}
