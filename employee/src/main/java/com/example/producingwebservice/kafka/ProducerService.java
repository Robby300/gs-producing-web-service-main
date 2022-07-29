package com.example.producingwebservice.kafka;


import com.example.producingwebservice.model.EmployeeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
	private final KafkaTemplate<String, EmployeeDto> kafkaTemplate;

	@Value("${topic.save}")
	private String topicAdd;

	public void produce(EmployeeDto employeeDto) {
		log.info("The produce got employeeDto: {}", employeeDto);
		kafkaTemplate.send(topicAdd, employeeDto.getUuid(), employeeDto);
		kafkaTemplate.flush();
		log.info("Topic posted");
	}
}
