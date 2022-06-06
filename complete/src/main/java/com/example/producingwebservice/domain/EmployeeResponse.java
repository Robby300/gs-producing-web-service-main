package com.example.producingwebservice.domain;

import com.example.producingwebservice.type.ResponseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmployeeResponse {
    private ResponseStatus responseStatus;
    private String message;
    private String payLoad;
}
