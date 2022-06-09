package com.example.producingwebservice.config;

import com.example.producingwebservice.entity.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    public static final int ZERO_ELEMENT = 0;
    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "${scheduler.cron}")
    public void deleteOneEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        if (!employees.isEmpty()) {
            employeeRepository.delete(employees.get(ZERO_ELEMENT));
        }
    }
}
