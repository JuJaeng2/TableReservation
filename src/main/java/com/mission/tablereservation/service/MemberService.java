package com.mission.tablereservation.service;

import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.model.CustomerDto;
import com.mission.tablereservation.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mission.tablereservation.exception.ErrorCode.NOT_EXIST_EMAIL;
import static com.mission.tablereservation.exception.ErrorCode.UNMATCH_PASSWORD;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)  {
        return this.customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_EXIST_EMAIL));
    }

    public CustomerDto authenticate(String email, String password){
        Customer customer = this.customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_EXIST_EMAIL));

        if (!this.passwordEncoder.matches(password, customer.getPassword())){
            throw new CustomException(UNMATCH_PASSWORD);
        }

        return CustomerDto.fromEntity(customer);
    }

    public List<UserType> countAuthorities(CustomerDto customerDto){
        List<UserType> roles = new ArrayList<>();
        for (String role : customerDto.getRoles()){
            roles.add(UserType.valueOf(role));
        }

        return roles;
    }
}
