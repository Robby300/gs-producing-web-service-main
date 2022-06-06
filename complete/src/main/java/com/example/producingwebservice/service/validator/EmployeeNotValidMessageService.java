package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {
    private final MessageService messageService;

    public String getNotValidSalaryMessage() {
        return messageService.getMessage("validation.salary.size");
    }

    public String getSalaryNotANumber() {
        return messageService.getMessage("validation.salary.not.number");
    }

    public String getNotValidSalaryByPositionMessage(Employee employee) {
        return MessageFormat.format(messageService.getMessage("validation.position.salary"),
                messageService.getMessage(employee.getPosition()),
                employee.getPosition().lowSalary,
                employee.getPosition().highSalary,
                employee.getSalary());
    }

    public String getNotValidCountsOfTasksMessage(Employee employee) {
        return MessageFormat.format(messageService.getMessage("validation.position.tasks"),
                messageService.getMessage(employee.getPosition()),
                employee.getPosition().maxTasks);
    }

    public String getNotNullMessage(String parameter) {
        return "parameter:" + messageService.getMessage("validation.not.null");
    }

    public String getNotValidNameLength() {
        return messageService.getMessage("validation.name.length");
    }
}
