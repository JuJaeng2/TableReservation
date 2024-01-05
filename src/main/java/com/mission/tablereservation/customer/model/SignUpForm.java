package com.mission.tablereservation.customer.model;


import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.customer.entity.Customer;
import lombok.*;

import java.time.LocalDate;
import java.util.Collections;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private String name;
    private String email;
    private String password;
    private LocalDate birth;
    private UserType userType;
    private String phone;

    public Customer toEntity(){
        return Customer.builder()
                .name(this.getName())
                .password(this.getPassword())
                .phone(this.getPhone())
                .roles(Collections.singletonList(String.valueOf(this.userType)))
                .email(this.getEmail())
                .birth(this.getBirth())
                .build();
    }
}
