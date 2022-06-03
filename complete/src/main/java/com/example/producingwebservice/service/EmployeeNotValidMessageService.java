package com.example.producingwebservice.service;

import com.example.producingwebservice.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    public static final String SALARY_SHOULD_BE_GREATER_THAN_0 = "salary should be greater than 0";
    public static final String FIELD_SHOULD_NOT_BE_NULL = "field should not be null";
    public static final String NAME_SHOULD_BE_BETWEEN_3_AND_32_CHARACTERS = "Name should be between 3 and 32 characters";
    private final MessageService messageService;

    public String getNotValidSalary(Employee employee) {
        return SALARY_SHOULD_BE_GREATER_THAN_0;
    }

    public String getNotValidSalaryByPositionMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage("validation.income.should.be.in.range.of")
                + employee.getPosition().lowSalary
                + messageService.getMessage("validation.to")
                + employee.getPosition().highSalary
                + messageService.getMessage("validation.sent.in.request")
                + employee.getSalary();
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage(employee.getPosition())
                + messageService.getMessage("validation.number.of.tasks.should.not.exceed")
                + employee.getPosition().maxTasks;
    }

    public String getNotNullMessage() {
        return FIELD_SHOULD_NOT_BE_NULL;
    }

    public String getNotValidNameLength() {
        return NAME_SHOULD_BE_BETWEEN_3_AND_32_CHARACTERS;
    }
}
