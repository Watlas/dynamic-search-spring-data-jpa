package com.dynamic.search.jpa.example.controller;

import com.dynamic.search.jpa.example.entity.Address;
import com.dynamic.search.jpa.example.repository.AddressRepository;
import com.dynamic.search.jpa.search.SpecificationBuilderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping(produces = "application/json")
    public List<Address> listAddressByFilter(String search) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search));
    }

    @GetMapping(produces = "application/json", path = "/page")
    public Page<Address> listAddressByFilterAndPageable(String search, Pageable pageable) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, search), pageable);
    }

}
