package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.domain.Employee;
import com.example.producingwebservice.domain.EmployeeResponse;
import com.example.producingwebservice.support.EmployeeChecker;
import com.example.producingwebservice.service.MessageService;
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
    private final MessageService messageService;
    private final EmployeeChecker employeeChecker;

    @Override
    public EmployeeResponse validate(Employee employee) {
        String validateFieldsMessage = validateFields(employee);
        if (validateFieldsMessage.isEmpty()) { //todo два раза идет вызов getPayLoadMessage. сделать один вызов // done
            log.debug("Employee {} passed check", employee);
            return getResponseBuild(employee.toString(), ResponseStatus.SUCCESS, "validation.employee.accepted");
        }

        log.debug("Employee {} failed verification and will not be added", employee);
        return getResponseBuild(validateFieldsMessage, ResponseStatus.FAILURE, "validation.employee.not.valid");
    }

    private String validateFields(Employee employee) { //todo название не стыкуется с тем что делает. будет лучше validateFields, можешь придумать свой вариант)) // done
        return Stream.of(employeeChecker.checkNameByNull(employee),
                        employeeChecker.checkSalary(employee),
                        employeeChecker.checkNameLength(employee),
                        employeeChecker.checkPositionByNull(employee),
                        employeeChecker.checkCountOfTasks(employee),
                        employeeChecker.checkSalaryByPosition(employee))
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .collect(Collectors.joining(SEPARATOR));
    }

    private EmployeeResponse getResponseBuild(String payLoad, ResponseStatus status, String message) {
        return EmployeeResponse.builder()
                .responseStatus(status)
                .message(messageService.getMessage(message))
                .payLoad(payLoad)
                .build();
    }
}
