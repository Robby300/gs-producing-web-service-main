package com.example.producingwebservice.config;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {
    //todo я бы сделал @Configuration + название SchedulerConfig + перенес в пакет config
    // done
    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "${scheduler.cron}") //todo вынести в проперти // done
    public void deleteOneEmployeePerMinute() {
        List<Employee> all = employeeRepository.findAll();
        if (all.size() > 0) {
            employeeRepository.delete(all.get(0));
        }
    }
}
