package com.example.producingwebservice.scheduller;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "0 * * * * *")
    public void deleteOneEmployeePerMinute() {
        List<Employee> all = employeeRepository.findAll();
        if (all.size() > 0) {
            employeeRepository.delete(all.get(0));
        }
    }
}
