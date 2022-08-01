package com.dynamic.search.jpa;

import com.dynamic.search.jpa.entity.Address;
import com.dynamic.search.jpa.entity.Country;
import com.dynamic.search.jpa.entity.People;
import com.dynamic.search.jpa.entity.State;
import com.dynamic.search.jpa.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DynamicSearchJpaApplication {

    private final AddressRepository addressRepository;

    public DynamicSearchJpaApplication(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DynamicSearchJpaApplication.class, args);
    }


    @Bean
    public void initDB() {
        Address address = new Address("Rua 1", new State("SP", new Country("Brasil")), new People("João"));
        addressRepository.save(address);
    }

}
