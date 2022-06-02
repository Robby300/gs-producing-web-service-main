package com.example.producingwebservice.domain;

import org.springframework.kafka.support.serializer.JsonSerializer;

public class EmployeeSerializer extends JsonSerializer<Employee> {
}
