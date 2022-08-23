package com.dynamic.search.jpa.example.controller;

import com.dynamic.search.jpa.example.entity.Address;
import com.dynamic.search.jpa.example.repository.AddressRepository;
import com.dynamic.search.jpa.search.SpecificationBuilderSearch;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(produces = "application/json")
    public List<Address> listAddressByFilterPost(@RequestBody JsonNode jsonNode) {
        return addressRepository.findAll(new SpecificationBuilderSearch<>(Address.class, jsonNode));
    }
}
