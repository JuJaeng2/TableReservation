package com.mission.tablereservation.model;

import com.mission.tablereservation.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private String name;
    private String password;
    private String phone;
    private String email;
    private List<String> roles;
    private LocalDate birth;

    public static CustomerDto fromEntity(Customer customer){
        return CustomerDto.builder()
                .name(customer.getName())
                .password(customer.getPassword())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .roles(customer.getRoles())
                .birth(customer.getBirth())
                .build();
    }
}
