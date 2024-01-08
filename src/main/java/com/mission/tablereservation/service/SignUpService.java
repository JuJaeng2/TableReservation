package com.mission.tablereservation.service;

import com.mission.tablereservation.common.model.MessageResponse;
import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.customer.model.CustomerDto;
import com.mission.tablereservation.customer.model.PartnerForm;
import com.mission.tablereservation.customer.model.SignUpForm;
import com.mission.tablereservation.customer.repository.CustomerRepository;
import com.mission.tablereservation.customer.service.MemberService;
import com.mission.tablereservation.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mission.tablereservation.config.UserType.CUSTOMER;
import static com.mission.tablereservation.config.UserType.PARTNER;
import static com.mission.tablereservation.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    /**
     * - 사용가능한 이메일인지 확인하고 패스워드를 암호화하여 유저 정보 저장 => 회원가입 완료
     */
    public MessageResponse signup(SignUpForm form) {

        if (isEmailExist(form.getEmail())) {
            throw new CustomException(ALREADY_REGISTERED_EMAIL);
        } else {
            String encodingPassword = passwordEncoder.encode(form.getPassword());

            form.setPassword(encodingPassword);
            form.setUserType(CUSTOMER);

            customerRepository.save(form.toEntity());
            return MessageResponse.builder()
                    .message("회원가입에 성공하셨습니다.")
                    .build();
        }

    }

    /**
     * 파트너 등록
     * - 파트너 등록을 신청한 유저의 토큰과 폼에 입력한 데이터가 일치하는지 확인하여 등록
     */
    public MessageResponse registerPartner(PartnerForm form, Authentication authentication) {

        String email = authentication.getName();

        CustomerDto customerDto = memberService.authenticate(email, form.getPassword());

        if (authentication.getAuthorities().size() > 1) {
            throw new CustomException(ALREADY_REGISTER_PARTNER);
        }

        Customer customer = customerRepository.findByEmail(customerDto.getEmail())
                .orElseThrow(() -> new CustomException(NOT_EXIST_EMAIL));

        // partner 권한 추가
        customer.getRoles().add(String.valueOf(PARTNER));

        customerRepository.save(customer);

        return MessageResponse.builder()
                .message("파트너가입에 성공하셨습니다. 다시 로그인을 해주세요.")
                .build();
    }

    /**
     * 이메일 존재 유무 확인
     */
    private boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

}
