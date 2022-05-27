package com.example.producingwebservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private long id;

    private String description;
}
