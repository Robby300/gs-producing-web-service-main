package com.example.producingwebservice.support;


import com.example.producingwebservice.model.EmployeeDto;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class EmployeeSerializer extends JsonSerializer<EmployeeDto> {}
