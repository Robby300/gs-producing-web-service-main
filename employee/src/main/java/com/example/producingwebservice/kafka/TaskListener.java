package com.example.producingwebservice.kafka;

import com.example.producingwebservice.entity.Employee;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskListener {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @KafkaListener(topics = "${topic.save}")
    public void executeTask(ConsumerRecord<String, EmployeeDto> task) {
        log.info("TaskListener. Request: key - {}, value - {}", task.key(), task.value());
        employeeRepository.save(modelMapper.map(task.value(), Employee.class));
    }
}
