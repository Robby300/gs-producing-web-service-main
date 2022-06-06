package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeNotValidMessageService {

    //todo поместить в ResourceBundle
    // done

    private final MessageService messageService;

    public String getNotValidSalaryMessage() {
        return messageService.getMessage("validation.salary.size");
    }

    public String getSalaryNotANumber() {
        return messageService.getMessage("validation.salary.not.number");
    }

    public String getNotValidSalaryByPositionMessage(Employee employee) {
        return messageService.getMessage("validation.at.position")
                + messageService.getMessage(employee.getPosition())
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
        return messageService.getMessage("validation.not.null");
    }

    public String getNotValidNameLength() {
        return messageService.getMessage("validation.name.length");
    }
}
