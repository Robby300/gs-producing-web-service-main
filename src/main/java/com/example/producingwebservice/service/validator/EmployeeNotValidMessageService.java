package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    public static final String VALIDATION_SALARY_SIZE = "validation.salary.size";
    public static final String VALIDATION_SALARY_NOT_NUMBER = "validation.salary.not.number";
    public static final String VALIDATION_POSITION_SALARY = "validation.position.salary";
    public static final String VALIDATION_POSITION_TASKS = "validation.position.tasks";
    public static final String VALIDATION_NOT_NULL = "validation.not.null";
    public static final String VALIDATION_NAME_LENGTH = "validation.name.length";
    private final MessageService messageService;

    public String getNotValidSalaryMessage() {
        return messageService.getMessage(VALIDATION_SALARY_SIZE);
    }

    public String getSalaryNotANumber() {
        return messageService.getMessage(VALIDATION_SALARY_NOT_NUMBER);
    }

    public String getNotValidSalaryByPositionMessage(EmployeeDto employeeDto) {
        return MessageFormat.format(messageService.getMessage(VALIDATION_POSITION_SALARY),
                messageService.getMessage(employeeDto.getPosition()),
                employeeDto.getPosition().lowSalary,
                employeeDto.getPosition().highSalary,
                employeeDto.getSalary());
    }

    public String getNotValidCountsOfTasksMessage(EmployeeDto employeeDto) {
        return MessageFormat.format(messageService.getMessage(VALIDATION_POSITION_TASKS),
                messageService.getMessage(employeeDto.getPosition()),
                employeeDto.getPosition().maxTasks);
    }

    public String getNotNullMessage(String parameter) {
        return parameter + messageService.getMessage(VALIDATION_NOT_NULL);
    }

    public String getNotValidNameLength() {
        return messageService.getMessage(VALIDATION_NAME_LENGTH);
    }
}
