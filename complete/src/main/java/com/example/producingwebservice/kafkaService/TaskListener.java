package com.example.producingwebservice.kafkaService;

import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListener {

    private final EmployeeService employeeService;

    @KafkaListener(topics = "${topic.save}")
    public void executeTask(ConsumerRecord<String, Employee> task) {
        log.info("TaskListener. Request: key - {}, value - {}", task.key(), task.value());
        employeeService.save(task.value());
    }
}
