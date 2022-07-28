package com.example.producingwebservice.checker;

import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.service.validator.EmployeeNotValidMessageService;
import com.example.producingwebservice.type.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeChecker {
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 48;
    public static final String SALARY_FIELD = "salary ";
    public static final String NAME_FIELD = "name ";
    public static final String POSITION_FIELD = "position ";
    private final EmployeeNotValidMessageService employeeNotValidMessageService;

    public String checkSalaryByPosition(EmployeeDto employeeDto) {
        if (isNotValidSalaryByPosition(employeeDto)) {
            return employeeNotValidMessageService.getNotValidSalaryByPositionMessage(employeeDto);
        }
        return null;
    }

    public String checkCountOfTasks(EmployeeDto employeeDto) {
        if (isNotValidCountOfTasks(employeeDto)) {
            return employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employeeDto);
        }
        return null;
    }

    public String checkPositionByNull(EmployeeDto employeeDto) {
        if (employeeDto.getPosition() == null) {
            return employeeNotValidMessageService.getNotNullMessage(POSITION_FIELD);
        }
        return null;
    }

    public String checkNameLength(EmployeeDto employeeDto) {
        if (employeeDto.getName().length() < MIN_NAME_LENGTH || employeeDto.getName().length() > MAX_NAME_LENGTH) {
            return employeeNotValidMessageService.getNotValidNameLength();
        }
        return null;
    }

    public String checkSalary(EmployeeDto employeeDto) {
        if (employeeDto.getSalary() == null) {
            return employeeNotValidMessageService.getNotNullMessage(SALARY_FIELD);
        }
        try {
            int salary = Integer.parseInt(employeeDto.getSalary());
            if (salary <= 0) {
                return employeeNotValidMessageService.getNotValidSalaryMessage();
            }
        } catch (NumberFormatException e) {
            return employeeNotValidMessageService.getSalaryNotANumber();
        }
        return null;
    }

    public String checkNameByNull(EmployeeDto employeeDto) {
        if (employeeDto.getName() == null) {
            return employeeNotValidMessageService.getNotNullMessage(NAME_FIELD);
        }
        return null;
    }

    private boolean isNotValidCountOfTasks(EmployeeDto employeeDto) {
        Position position = employeeDto.getPosition();
        return !position.isValidCountOfTasks(employeeDto.getTasks().size());
    }

    private boolean isNotValidSalaryByPosition(EmployeeDto employeeDto) {
        return !employeeDto.getPosition().isValidSalary(employeeDto.getSalary());
    }
}
