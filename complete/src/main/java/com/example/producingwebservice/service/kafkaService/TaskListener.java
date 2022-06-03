package com.example.producingwebservice.service.kafkaService;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListener {

    private final EmployeeRepository employeeRepository;

    @KafkaListener(topics = "${topic.save}")
    public void executeTask(ConsumerRecord<String, Employee> task) {
        log.info("TaskListener. Request: key - {}, value - {}", task.key(), task.value());
        employeeRepository.save(task.value());
    }
}
