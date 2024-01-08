package com.mission.tablereservation.customer.service;

import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.model.CustomerDto;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.exception.CustomException;
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

    /**
     *- 이메일을 통해 유저 정보를 로드
     */
    @Override
    public UserDetails loadUserByUsername(String email)  {
        return getCustomerByEmail(email);
    }

    /**
     * 옳은 회원정보인지 확인
     */
    public CustomerDto authenticate(String email, String password){
        Customer customer = getCustomerByEmail(email);

        if (!passwordEncoder.matches(password, customer.getPassword())){
            throw new CustomException(UNMATCH_PASSWORD);
        }

        return CustomerDto.fromEntity(customer);
    }

    /**
     * 로그인한 유저의 권한이 어떤게 있는지 알기위한 메서드
     * - 권한 List를 반환
     */
    public List<UserType> countAuthorities(CustomerDto customerDto){
        List<UserType> roles = new ArrayList<>();
        for (String role : customerDto.getRoles()){
            roles.add(UserType.valueOf(role));
        }

        return roles;
    }

    /**
     * 이메일을 통해 customer를 찾는 메서드
     */
    private Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_EXIST_EMAIL));
    }
}
