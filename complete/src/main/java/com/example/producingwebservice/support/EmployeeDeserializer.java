package com.example.producingwebservice.support;

import com.example.producingwebservice.entity.Employee;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class EmployeeDeserializer extends JsonDeserializer<Employee> {
}