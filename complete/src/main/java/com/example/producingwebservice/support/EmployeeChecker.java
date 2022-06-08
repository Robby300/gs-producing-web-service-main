package com.example.producingwebservice.support;
//todo переместить в отдельный пакет checker
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.service.validator.EmployeeNotValidMessageService;
import com.example.producingwebservice.type.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeChecker {
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 32;
    public static final String SALARY_FIELD = "Salary";
    public static final String NAME_FIELD = "name";
    public static final String POSITION_FIELD = "Position";
    private final EmployeeNotValidMessageService employeeNotValidMessageService;

    public String checkSalaryByPosition(Employee employee) {
        if (isNotValidSalaryByPosition(employee)) {
            return employeeNotValidMessageService.getNotValidSalaryByPositionMessage(employee);
        }
        return null;
    }

    public String checkCountOfTasks(Employee employee) {
        if (isNotValidCountOfTasks(employee)) {
            return employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employee);
        }
        return null;
    }

    public String checkPositionByNull(Employee employee) {
        if (employee.getPosition() == null) {
            return employeeNotValidMessageService.getNotNullMessage(POSITION_FIELD);
        }
        return null;
    }

    public String checkNameLength(Employee employee) {
        if (employee.getName().length() < MIN_NAME_LENGTH || employee.getName().length() > MAX_NAME_LENGTH) {
            return employeeNotValidMessageService.getNotValidNameLength();
        }
        return null;
    }

    public String checkSalary(Employee employee) {
        if (employee.getSalary() == null) {
            return employeeNotValidMessageService.getNotNullMessage(SALARY_FIELD);
        }
        try {
            int salary = Integer.parseInt(employee.getSalary());
            if (salary <= 0) {
                return employeeNotValidMessageService.getNotValidSalaryMessage();
            }
        } catch (NumberFormatException e) {
            return employeeNotValidMessageService.getSalaryNotANumber();
        }
        return null;
    }

    public String checkNameByNull(Employee employee) {
        if (employee.getName() == null) {
            return employeeNotValidMessageService.getNotNullMessage(NAME_FIELD);
        }
        return null;
    }

    private boolean isNotValidCountOfTasks(Employee employee) {
        Position position = employee.getPosition();
        return !position.isValidCountOfTasks(employee.getTasks().size());
    }

    private boolean isNotValidSalaryByPosition(Employee employee) {
        return !employee.getPosition().isValidSalary(employee.getSalary());
    }
}
