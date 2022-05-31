package com.example.producingwebservice.service;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeePosition;
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
    private final ValidMessageByPositionService validMessageByPositionService;

    @Override
    public boolean isValidInput(Employee employee) {
        if (isNotValidSalary(employee) || isNotValidCountOfTasks(employee)) {
            return false;
        }
        return getConstraintViolations(employee).isEmpty();
    }

    @Override
    public String getViolationsMessage(Employee employee) {
        StringBuilder result = new StringBuilder();
        getConstraintViolations(employee)
                .forEach(violation -> result.append(violation.getMessageTemplate())
                        .append(SEPARATOR));
        if (isNotValidSalary(employee)) {
            result.append(validMessageByPositionService.getNotValidSalaryMessage(employee)).append(SEPARATOR);
        }
        if (isNotValidCountOfTasks(employee)) {
            result.append(validMessageByPositionService.getNotValidCountsOfTasksMessage(employee)).append(SEPARATOR);
        }
        return result.toString();
    }

    private Set<ConstraintViolation<Employee>> getConstraintViolations(Employee employee) {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(employee);
    }

    private boolean isNotValidCountOfTasks(Employee employee) {
        EmployeePosition employeePosition = employee.getEmployeePosition();
        return !employeePosition.isValidCountOfTasks(employee.getTasks().size());
    }

    private boolean isNotValidSalary(Employee employee) {
        return !employee.getEmployeePosition().isValidSalary(employee.getSalary());
    }
}
