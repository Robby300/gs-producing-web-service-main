package com.example.producingwebservice.kafka;

import com.example.producingwebservice.entity.Employee;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerListener {

	private static final String DEDUP_PREFIX = "kafka:dedup:saveAll:";
	private static final long DEDUP_TTL_HOURS = 1;
	private final EmployeeRepository employeeRepository;
	private final StringRedisTemplate stringRedisTemplate;

	@KafkaListener(topics = "${topic.save}")
	public void executeTask(ConsumerRecord<String, EmployeeDto> task) {
		log.info("TaskListener. Request: key - {}, value - {}", task.key(), task.value());
		EmployeeDto dto = task.value();

		if (dto.getUuid() != null) {
			String dedupKey = DEDUP_PREFIX + dto.getUuid();
			Boolean firstTime = stringRedisTemplate.opsForValue()
					.setIfAbsent(dedupKey, "processed", Duration.ofHours(DEDUP_TTL_HOURS));
			if (Boolean.FALSE.equals(firstTime)) {
				log.warn("Duplicate message detected for UUID: {}", dto.getUuid());
				return;
			}
		}

		Employee employee = new Employee(
				null, dto.getUuid(), dto.getName(), dto.getSalary(),
				dto.getPosition(), dto.getTasks());
		employeeRepository.save(employee);
	}
}
