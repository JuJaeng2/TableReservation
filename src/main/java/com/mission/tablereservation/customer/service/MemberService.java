package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.model.SignInForm;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.customer.model.CustomerDto;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)  {
        return getCustomerByEmail(email);
    }

    public CustomerDto authenticate(String email, String password){
        Customer customer = getCustomerByEmail(email);

        if (!passwordEncoder.matches(password, customer.getPassword())){
            throw new CustomException(UNMATCH_PASSWORD);
        }

        return CustomerDto.fromEntity(customer);
    }

    public CustomerDto authenticate(SignInForm signInForm){
        Customer customer = getCustomerByEmail(signInForm.getEmail());

        if (!passwordEncoder.matches(signInForm.getPassword(), customer.getPassword())){
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



    private Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_EXIST_EMAIL));
    }
}
