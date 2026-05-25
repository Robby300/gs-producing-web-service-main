package com.example.producingwebservice.kafka;

import com.example.producingwebservice.entity.Employee;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerListener {

	private final EmployeeRepository employeeRepository;

	@KafkaListener(topics = "${topic.save}")
	public void executeTask(ConsumerRecord<String, EmployeeDto> task) {
		log.info("TaskListener. Request: key - {}, value - {}", task.key(), task.value());
		EmployeeDto dto = task.value();
		Employee employee = new Employee(
				null, dto.getUuid(), dto.getName(), dto.getSalary(),
				dto.getPosition(), dto.getTasks());
		employeeRepository.save(employee);
	}
}
