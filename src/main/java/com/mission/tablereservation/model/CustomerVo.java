package com.mission.tablereservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVo {

    private String name;
    private String email;

    public static CustomerVo createCustomerVo(String name, String email){
        return CustomerVo.builder()
                .name(name)
                .email(email)
                .build();
    }

}
