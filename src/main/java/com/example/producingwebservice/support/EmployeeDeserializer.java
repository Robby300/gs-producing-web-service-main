package com.example.producingwebservice.support;

import com.example.producingwebservice.model.EmployeeDto;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class EmployeeDeserializer extends JsonDeserializer<EmployeeDto> {
}