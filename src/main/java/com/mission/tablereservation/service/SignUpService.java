package com.mission.tablereservation.service;

import com.mission.tablereservation.config.UserType;
import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.model.CustomerDto;
import com.mission.tablereservation.model.PartnerForm;
import com.mission.tablereservation.model.SignUpForm;
import com.mission.tablereservation.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SignUpService{

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    public String signup(SignUpForm form) {

        if (isEmailExist(form.getEmail())){
            throw new CustomException(ALREADY_REGISTERED_EMAIL);
        }else {
            String EncodingPw = passwordEncoder.encode(form.getPassword());
            form.setPassword(EncodingPw);
            form.setUserType(UserType.CUSTOMER);
            customerRepository.save(form.toEntity());
            return "회원가입에 성공하셨습니다.";
        }

    }

    private boolean isEmailExist(String email){
        return customerRepository.findByEmail(email).isPresent();
    }

    public String registerPartner(PartnerForm form, Authentication authentication){

        CustomerDto customerDto = memberService.authenticate(form.getEmail(), form.getPassword());
        if(authentication.getAuthorities().size() > 1){
            throw new CustomException(ALREADY_REGISTER_PARTNER);
        }

        Customer customer = customerRepository.findByEmail(customerDto.getEmail())
                .orElseThrow(() -> new CustomException(NOT_EXIST_EMAIL));
        customer.getRoles().add(String.valueOf(UserType.PARTNER));
        customerRepository.save(customer);

        return "파트너가입에 성공하셨습니다. 다시 로그인을 해주세요.";
    }

    private boolean identityVerification(PartnerForm form) {

        return false;
    }
}
