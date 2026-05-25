package com.example.producingwebservice.config;


import com.example.producingwebservice.entity.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
	public static final int ZERO_ELEMENT = 0;
	private static final String LOCK_KEY = "lock:scheduler:deleteOneEmployee";
	private static final Duration LOCK_TTL = Duration.ofSeconds(60);
	private final EmployeeRepository employeeRepository;
	private final StringRedisTemplate stringRedisTemplate;

	@Scheduled(cron = "${scheduler.cron}")
	public void deleteOneEmployee() {
		Boolean acquired = stringRedisTemplate.opsForValue()
				.setIfAbsent(LOCK_KEY, "locked", LOCK_TTL);
		if (Boolean.FALSE.equals(acquired)) {
			log.info("Scheduler lock not acquired, another instance is handling deletion");
			return;
		}
		try {
			List<Employee> employees = employeeRepository.findAll();
			if (!employees.isEmpty()) {
				employeeRepository.delete(employees.get(ZERO_ELEMENT));
				log.info("Deleted employee {}", employees.get(ZERO_ELEMENT).getUuid());
			}
		} finally {
			stringRedisTemplate.delete(LOCK_KEY);
		}
	}
}
