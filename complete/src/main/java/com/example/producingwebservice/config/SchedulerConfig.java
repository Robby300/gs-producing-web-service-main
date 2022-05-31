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
    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "${scheduler.cron}")
    public void deleteOneEmployeePerMinute() { //todo у тебя название зависит от значения в аннотации то есть если поменять cron у тебя будет неожиданный результат
        List<Employee> all = employeeRepository.findAll();
        if (all.size() > 0) { //todo волшебная цифра. Выведи в константу
            employeeRepository.delete(all.get(0));
        }
    }
}
