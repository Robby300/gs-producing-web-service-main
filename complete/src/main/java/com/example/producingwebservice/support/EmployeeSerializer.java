package com.example.producingwebservice.support;

import com.example.producingwebservice.domain.Employee;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class EmployeeSerializer extends JsonSerializer<Employee> {
}
