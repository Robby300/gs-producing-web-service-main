package com.example.producingwebservice;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.type.EmployeePosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class KafkaTest {
//    private final KafkaTemplate kafkaTemplate;
//    Employee employee = new Employee(1L, "123uuid", "Oleg", 140_000, EmployeePosition.MANAGER, null);
//
//    @Autowired
//    public KafkaTest(KafkaTemplate kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    @Test
//    void shouldSaveEmployeeViaKafka() {
//
//        kafkaTemplate.send("save", employee);
//        Assertions.assertTrue(employee.getId().equals(1L));
//    }
}
