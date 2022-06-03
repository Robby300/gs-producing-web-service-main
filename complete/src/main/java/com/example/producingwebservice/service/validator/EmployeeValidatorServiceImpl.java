package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;
import com.example.producingwebservice.type.Position;
import com.example.producingwebservice.type.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeValidatorServiceImpl implements EmployeeValidatorService {

    public static final String SEPARATOR = "; ";
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 32;
    public static final String EMPLOYEE_ACCEPTED = "Employee accepted";
    public static final String EMPLOYEE_NOT_VALID = "Employee not valid";
    private final EmployeeNotValidMessageService employeeNotValidMessageService;

    private final StringBuilder messageBuilder = new StringBuilder();

    private boolean isValidInput(Employee employee) {
        boolean isValid = true;
        if (employee.getName() == null) {
            isValid = false;
            employee.setName(employeeNotValidMessageService.getNotNullMessage());
        }
        if (employee.getSalary() == null) {
            isValid = false;
            employee.setSalary(employeeNotValidMessageService.getNotNullMessage());
        }
        try {
            int salary = Integer.parseInt(employee.getSalary());
            if (salary == 0) {
                isValid = false;
                employee.setSalary(employeeNotValidMessageService.getNotValidSalaryMessage());
            }
        } catch (NumberFormatException e) {
            isValid = false;
            employee.setSalary(employeeNotValidMessageService.getSalaryNotANumber());
        }

        if (employee.getName().length() < MIN_NAME_LENGTH || employee.getName().length() > MAX_NAME_LENGTH) {
            isValid = false;
            employee.setName(employeeNotValidMessageService.getNotValidNameLength());
        }
        if (employee.getPosition() == null) {
            isValid = false;
            messageBuilder.append("Position: ").append(employeeNotValidMessageService.getNotNullMessage());
        }
        if (isNotValidCountOfTasks(employee)) {
            isValid = false;
            messageBuilder.append(employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employee))
                    .append(SEPARATOR);
        }
        if (isNotValidSalaryByPosition(employee)) {
            isValid = false;
            employee.setSalary(employeeNotValidMessageService.getNotValidSalaryByPositionMessage(employee));
        }
        return isValid;
    }

    private boolean isNotValidCountOfTasks(Employee employee) {
        Position position = employee.getPosition();
        return !position.isValidCountOfTasks(employee.getTasks().size());
    }

    private boolean isNotValidSalaryByPosition(Employee employee) {
        return !employee.getPosition().isValidSalary(employee.getSalary());
    }

    @Override
    public EmployeeResponse validate(Employee employee) {

        if (isValidInput(employee)) {
            log.debug("Employee {} passed check", employee);
            return EmployeeResponse.builder()
                    .responseStatus(ResponseStatus.SUCCESS)
                    .message(EMPLOYEE_ACCEPTED)
                    .employee(employee)
                    .build();
        }

        log.debug("Employee {} failed verification and will not be added", employee);
        String message = messageBuilder.toString();
        messageBuilder.setLength(0);
        return EmployeeResponse.builder()
                .responseStatus(ResponseStatus.FAILURE)
                .message(message.isEmpty() ? EMPLOYEE_NOT_VALID : message)
                .employee(employee)
                .build();
    }
}
