package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.checker.EmployeeChecker;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
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
    public EmployeeResponse validate(EmployeeDto employeeDto) {
        String validateFieldsMessage = validateFields(employeeDto);
        if (validateFieldsMessage.isEmpty()) {
            log.debug("Employee {} passed check", employeeDto);
            return getResponseBuild(employeeDto.toString(), ResponseStatus.SUCCESS, "validation.employee.accepted");
        }

        log.debug("Employee {} failed verification and will not be added", employeeDto);
        return getResponseBuild(validateFieldsMessage, ResponseStatus.FAILURE, "validation.employee.not.valid");
    }

    private String validateFields(EmployeeDto employeeDto) {
        return Stream.of(employeeChecker.checkNameByNull(employeeDto),
                        employeeChecker.checkSalary(employeeDto),
                        employeeChecker.checkNameLength(employeeDto),
                        employeeChecker.checkPositionByNull(employeeDto),
                        employeeChecker.checkCountOfTasks(employeeDto),
                        employeeChecker.checkSalaryByPosition(employeeDto))
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
