package com.mission.tablereservation.partner.entity;

import com.mission.tablereservation.entity.BaseEntity;
import com.mission.tablereservation.entity.Customer;
import com.mission.tablereservation.partner.model.StoreForm;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    private String description;

    @ManyToOne
    @JoinColumn
    private Customer customer;

    public static Store createStore(StoreForm storeForm, Customer customer){
        return Store.builder()
                .name(storeForm.getName())
                .location(storeForm.getLocation())
                .description(storeForm.getDescription())
                .customer(customer)
                .build();
    }

}
