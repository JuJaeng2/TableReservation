package com.mission.tablereservation.partner.service;

import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.model.StoreForm;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.repository.StoreRepository;
import com.mission.tablereservation.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mission.tablereservation.exception.ErrorCode.NOT_FOUND_PARTNER;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public StoreResponse registerStore(StoreForm storeForm, Authentication authentication){

        String email = authentication.getName();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PARTNER));

        Store store = storeRepository.save(Store.createStore(storeForm, customer));

        return StoreResponse.toResponse(store);
    }


}
