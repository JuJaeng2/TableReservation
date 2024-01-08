package com.mission.tablereservation.partner.repository;

import com.mission.tablereservation.customer.entity.Customer;
import com.mission.tablereservation.partner.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String storeName);

    int countByNameAndCustomer(String name, Customer customer);
}
