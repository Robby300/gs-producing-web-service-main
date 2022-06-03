package com.example.producingwebservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmployeeResponse {
    private String status;
    private String message;
    private Employee employee;
}
