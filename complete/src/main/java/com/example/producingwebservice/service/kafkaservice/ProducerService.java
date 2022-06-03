package com.example.producingwebservice.service.kafkaservice;


import com.example.producingwebservice.domain.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<String, Employee> kafkaTemplate;
    @Value("${topic.save}")
    private String topicAdd;

    public void produce(Employee employee) {
        log.info("The produce got employee: {}", employee);
        kafkaTemplate.send(topicAdd, employee.getUuid(), employee);
        kafkaTemplate.flush();
        log.debug("Topic posted");
    }
}
