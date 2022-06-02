package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    private final MessageService messageService;

    public String getNotValidSalaryMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage("validation.income.should.be.in.range.of")
                + employee.getEmployeePosition().lowSalary
                + messageService.getMessage("validation.to")
                + employee.getEmployeePosition().highSalary
                + messageService.getMessage("validation.sent.in.request")
                + employee.getSalary();
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage(employee.getEmployeePosition())
                + messageService.getMessage("validation.number.of.tasks.should.not.exceed")
                + employee.getEmployeePosition().maxTasks;
    }

}
