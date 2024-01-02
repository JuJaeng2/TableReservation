package com.mission.tablereservation.service;

import com.mission.tablereservation.exception.CustomException;
import com.mission.tablereservation.partner.entity.Store;
import com.mission.tablereservation.partner.model.StoreResponse;
import com.mission.tablereservation.partner.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mission.tablereservation.exception.ErrorCode.NOT_FOUND_STORE;

@Service
@RequiredArgsConstructor
public class StoreService {

    public final StoreRepository storeRepository;

    public StoreResponse getStoreInfo(String storeName){
        Store store = storeRepository.findByName(storeName)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

        return StoreResponse.toResponse(store);
    }
}
