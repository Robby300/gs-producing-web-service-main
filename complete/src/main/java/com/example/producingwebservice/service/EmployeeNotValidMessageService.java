package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    private final MessageService messageService;

    public String getNotValidSalaryMessage(Employee employee) { //todo кириллица в коде. // done
        return messageService.getMessage("AT_THE_POSITION")
                + messageService.getMessage("INCOME_MUST_BE_IN_RANGE_OF")
                + employee.getEmployeePosition().lowSalary
                + messageService.getMessage("TO")
                + employee.getEmployeePosition().highSalary
                + messageService.getMessage("SENT_IN_A_REQUEST")
                + employee.getSalary();
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) { //todo кириллица в коде.
        return messageService.getMessage("AT_THE_POSITION")
                + messageService.getMessage(employee.getEmployeePosition())
                + messageService.getMessage("THE_NUMBER_OF_TASKS_SHOULD_NOT_EXCEED")
                + employee.getEmployeePosition().maxTasks;
    }

}
