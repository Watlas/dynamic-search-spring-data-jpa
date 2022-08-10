package com.dynamic.search.jpa.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @CreationTimestamp
    private LocalDateTime createdAt;

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
