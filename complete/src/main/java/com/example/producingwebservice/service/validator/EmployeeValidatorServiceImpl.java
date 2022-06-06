package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;
import com.example.producingwebservice.service.MessageService;
import com.example.producingwebservice.type.Position;
import com.example.producingwebservice.type.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeValidatorServiceImpl implements EmployeeValidatorService {

    public static final String SEPARATOR = "; ";
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 32;
    private final EmployeeNotValidMessageService employeeNotValidMessageService;
    private final MessageService messageService;

    @Override
    public EmployeeResponse validate(Employee employee) {

        if (getPayLoadMessage(employee).isEmpty()) {
            log.debug("Employee {} passed check", employee);
            return getResponseBuild(employee.toString(), ResponseStatus.SUCCESS, "validation.employee.accepted");
        }

        log.debug("Employee {} failed verification and will not be added", employee);
        return getResponseBuild(getPayLoadMessage(employee), ResponseStatus.FAILURE, "validation.employee.not.valid");
    }

    private String getPayLoadMessage(Employee employee) {

        return Stream.of(checkNameByNull(employee),
                        checkSalary(employee),
                        checkNameLength(employee),
                        checkPositionByNull(employee),
                        checkCountOfTasks(employee),
                        checkSalaryByPosition(employee))
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .collect(Collectors.joining(SEPARATOR));
    }

    private String checkSalaryByPosition(Employee employee) {
        if (isNotValidSalaryByPosition(employee)) {
            return employeeNotValidMessageService.getNotValidSalaryByPositionMessage(employee);
        }
        return null;
    }

    private String checkCountOfTasks(Employee employee) {
        if (isNotValidCountOfTasks(employee)) {
            return employeeNotValidMessageService.getNotValidCountsOfTasksMessage(employee);
        }
        return null;
    }

    private String checkPositionByNull(Employee employee) {
        if (employee.getPosition() == null) {
            return employeeNotValidMessageService.getNotNullMessage("Position");
        }
        return null;
    }

    private String checkNameLength(Employee employee) {
        if (employee.getName().length() < MIN_NAME_LENGTH || employee.getName().length() > MAX_NAME_LENGTH) {
            return employeeNotValidMessageService.getNotValidNameLength();
        }
        return null;
    }

    private String checkSalary(Employee employee) {
        if (employee.getSalary() == null) {
            return employeeNotValidMessageService.getNotNullMessage("Salary");
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

    private String checkNameByNull(Employee employee) {
        if (employee.getName() == null) {
            return employeeNotValidMessageService.getNotNullMessage("name");
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

    private EmployeeResponse getResponseBuild(String payLoad, ResponseStatus status, String message) {
        return EmployeeResponse.builder()
                .responseStatus(status)
                .message(messageService.getMessage(message))
                .payLoad(payLoad)
                .build();
    }
}
