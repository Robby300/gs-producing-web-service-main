package com.example.producingwebservice.service.validator;

import com.example.producingwebservice.api.EmployeeValidatorService;
import com.example.producingwebservice.checker.EmployeeChecker;
import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import com.example.producingwebservice.service.MessageService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Set;

import static com.example.producingwebservice.testData.EmployeeTestData.getFirstEmployeeDto;
import static com.example.producingwebservice.testData.TaskTestData.*;
import static com.example.producingwebservice.type.ResponseStatus.FAILURE;
import static com.example.producingwebservice.type.ResponseStatus.SUCCESS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class EmployeeValidatorServiceImplTest {

    public static final String SHORT_NAME = "Ab";
    public static final String LONG_NAME = "Ahmed Ibn Hamid All Rasul Fatih Ali Babaevich Ibn Fadlan";
    public static final String LOW_SALARY = "10000";
    MessageService messageService = new MessageService(new ResourceBundleMessageSource());
    private final EmployeeValidatorService employeeValidatorService =
            new EmployeeValidatorServiceImpl(messageService, new EmployeeChecker(new EmployeeNotValidMessageService(messageService)));
    ModelMapper modelMapper = new ModelMapper();

    @Test
    void shouldGetSuccessStatus() {
        EmployeeResponse validateResponse = employeeValidatorService.validate(getFirstEmployeeDto());
        assertThat(validateResponse.getResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void shouldGetFailureStatusBecauseShortName() {
        EmployeeDto employeeDtoForValidate = getFirstEmployeeDto();
        employeeDtoForValidate.setName(SHORT_NAME);
        EmployeeResponse validateResponse = employeeValidatorService.validate(employeeDtoForValidate);
        assertThat(validateResponse.getResponseStatus()).isEqualTo(FAILURE);
        assertThat(validateResponse.getPayLoad()).isEqualTo("validation.name.length");
    }

    @Test
    void shouldGetFailureStatusBecauseLongName() {
        EmployeeDto employeeDtoForValidate = getFirstEmployeeDto();
        employeeDtoForValidate.setName(LONG_NAME);
        EmployeeResponse validateResponse = employeeValidatorService.validate(employeeDtoForValidate);
        assertThat(validateResponse.getResponseStatus()).isEqualTo(FAILURE);
        assertThat(validateResponse.getPayLoad()).isEqualTo("validation.name.length");
    }

    @Test
    void shouldGetFailureStatusBecauseLowSalaryForEmployeePosition() {
        EmployeeDto employeeDtoForValidate = getFirstEmployeeDto();
        employeeDtoForValidate.setSalary(LOW_SALARY);
        EmployeeResponse validateResponse = employeeValidatorService.validate(employeeDtoForValidate);
        assertThat(validateResponse.getResponseStatus()).isEqualTo(FAILURE);
        assertThat(validateResponse.getPayLoad()).isEqualTo("validation.position.salary");
    }

    @Test
    void shouldGetFailureStatusBecauseManyTasksForEmployeePosition() {
        EmployeeDto employeeDtoForValidate = getFirstEmployeeDto();
        Set<Task> threeTasks = Set.of(
                modelMapper.map(getFirstTask(), Task.class),
                modelMapper.map(getSecondTask(), Task.class),
                modelMapper.map(getThirdTask(), Task.class));
        employeeDtoForValidate.setTasks(threeTasks);

        EmployeeResponse validateResponse = employeeValidatorService.validate(employeeDtoForValidate);

        assertThat(validateResponse.getResponseStatus()).isEqualTo(FAILURE);
        assertThat(validateResponse.getPayLoad()).isEqualTo("validation.position.tasks");
    }
}