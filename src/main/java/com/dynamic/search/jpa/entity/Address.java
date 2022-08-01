package com.dynamic.search.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn
    private State state;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn
    private People people;

    public Address(String name, State state, People people) {
        this.name = name;
        this.state = state;
        this.people = people;
    }
}
