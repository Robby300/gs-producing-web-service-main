package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    private final MessageService messageService;
    public String getNotValidSalaryMessage(Employee employee) { //todo кириллица в коде.
        return MessageFormat.format("У позиции {0} зп должна быть в диапазоне от {1} до {2}, в запросе прислали {3}",
                messageService.getMessage(employee.getEmployeePosition()),
                employee.getEmployeePosition().lowSalary,
                employee.getEmployeePosition().highSalary,
                employee.getSalary());
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) { //todo кириллица в коде.
        return MessageFormat.format("У позиции {0} количество задач должно быть не более {1}",
                messageService. getMessage(employee.getEmployeePosition()),
                employee.getEmployeePosition().maxTasks);
    }

}
