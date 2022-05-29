package com.example.producingwebservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
}
