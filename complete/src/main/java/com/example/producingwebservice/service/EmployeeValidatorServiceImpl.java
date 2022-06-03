package com.example.producingwebservice.service;

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
    private final EmployeeNotValidMessageService employeeNotValidMessageService;
    private final MessageService messageService;

    private final StringBuilder message = new StringBuilder();

    @Override
    public boolean isValidInput(Employee employee) {
        boolean isValid = true;
        if (employee.getName() == null) {
            isValid = false;
            employee.setName(employeeNotValidMessageService.getNotNullMessage());
        }
        if (employee.getSalary() == null) {
            isValid = false;
            employee.setSalary(employeeNotValidMessageService.getNotNullMessage());
        }
        if (employee.getName().length() < 3 || employee.getName().length() > 32) {
            isValid = false;
            employee.setName(employeeNotValidMessageService.getNotValidNameLength());
        }
        if (isNotValidCountOfTasks(employee)) {
            isValid = false;
            message.append(employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employee))
                    .append(SEPARATOR);
        }
        if (isNotValidSalaryByPosition(employee)) {
            isValid = false;
            message.append(employeeNotValidMessageService.getNotValidSalaryByPositionMessage(employee))
                    .append(SEPARATOR);
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
                    .responseStatus(ResponseStatus.FAILURE)
                    .message("Employee accepted")
                    .employee(employee)
                    .build();
        }

        log.debug("Employee {} failed verification and will not be added", employee);
        return EmployeeResponse.builder()
                .responseStatus(ResponseStatus.SUCCESS)
                .message(message.capacity() > 0 ? message.toString():"Employee not valid")
                .employee(employee)
                .build();

    }
}
