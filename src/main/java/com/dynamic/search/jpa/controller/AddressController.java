package com.dynamic.search.jpa.controller;

import com.dynamic.search.jpa.entity.Address;
import com.dynamic.search.jpa.repository.AddressRepository;
import com.dynamic.search.jpa.search.SpecificationBuilderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/address")
@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;


    @GetMapping()
    public List<Address> listEndereco(@RequestParam String search) {

        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search));
    }


}
