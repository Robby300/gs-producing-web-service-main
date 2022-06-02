package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.type.EmployeePosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Service
@RequiredArgsConstructor
public class EmployeeValidatorServiceImpl implements EmployeeValidatorService {

    public static final String SEPARATOR = "; ";
    private final EmployeeNotValidMessageService employeeNotValidMessageService;
    private final MessageService messageService;

    @Override
    public boolean isValidInput(Employee employee) {
        if (isNotValidSalary(employee) || isNotValidCountOfTasks(employee)) {
            return false;
        }
        return getConstraintViolations(employee).isEmpty();
    }

    @Override
    public String getViolationsMessage(Employee employee) {
        StringBuilder violationsMessage = new StringBuilder();
        Set<ConstraintViolation<Employee>> employeeConstraintViolations = getConstraintViolations(employee);
        employeeConstraintViolations
                .forEach(violation -> violationsMessage.append(violation.getMessageTemplate())
                        .append(SEPARATOR));
        addNotValidSalaryMessage(employee, violationsMessage);
        addNotValidCountsOfTasksMessage(employee, violationsMessage);
        return violationsMessage.toString();
    }

    private Set<ConstraintViolation<Employee>> getConstraintViolations(Employee employee) {
        Validator validator;
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        return validator.validate(employee);
    }

    private boolean isNotValidCountOfTasks(Employee employee) {
        EmployeePosition employeePosition = employee.getEmployeePosition();
        return !employeePosition.isValidCountOfTasks(employee.getTasks().size());
    }

    private boolean isNotValidSalary(Employee employee) {
        return !employee.getEmployeePosition().isValidSalary(employee.getSalary());
    }

    private void addNotValidCountsOfTasksMessage(Employee employee, StringBuilder result) {
        if (isNotValidCountOfTasks(employee)) {
            result.append(employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employee)).append(SEPARATOR);
        }
    }

    private void addNotValidSalaryMessage(Employee employee, StringBuilder result) {
        if (isNotValidSalary(employee)) {
            result.append(employeeNotValidMessageService.getNotValidSalaryMessage(employee)).append(SEPARATOR);
        }
    }

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
