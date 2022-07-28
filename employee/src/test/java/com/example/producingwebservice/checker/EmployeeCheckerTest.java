package com.example.producingwebservice.checker;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.service.MessageService;
import com.example.producingwebservice.service.validator.EmployeeNotValidMessageService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Set;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static com.example.producingwebservice.testData.TaskTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EmployeeCheckerTest {

    private final EmployeeChecker employeeChecker = new EmployeeChecker(
            new EmployeeNotValidMessageService(
                    new MessageService(
                            new ResourceBundleMessageSource())));

    ModelMapper modelMapper = new ModelMapper();

    @Test
    void shouldCheckSalaryByPosition() {
        EmployeeDto employeeDtoForSave = getEmployeeDtoForSaveInController();
        employeeDtoForSave.setSalary(ONE_MILLION_SALARY);
        String checkMessage = employeeChecker.checkSalaryByPosition(employeeDtoForSave);
        assertThat(checkMessage).isNotNull().isEqualTo("validation.position.salary");
    }

    @Test
    void shouldCheckCountOfTasks() {
        EmployeeDto employeeDtoForSave = getEmployeeDtoForSaveInController();
        employeeDtoForSave.setTasks(Set.of(
                modelMapper.map(getFirstTask(), Task.class),
                modelMapper.map(getSecondTask(), Task.class),
                modelMapper.map(getThirdTask(), Task.class)));
        String checkMessage = employeeChecker.checkCountOfTasks(employeeDtoForSave);
        assertThat(checkMessage).isNotNull().isEqualTo("validation.position.tasks");
    }

    @Test
    void shouldCheckPositionByNull() {
        EmployeeDto employeeDtoForSave = getEmployeeDtoForSaveInController();
        employeeDtoForSave.setPosition(null);
        String checkMessage = employeeChecker.checkPositionByNull(employeeDtoForSave);
        assertThat(checkMessage).isNotNull().isEqualTo("position validation.not.null");
    }

    @Test
    void shouldCheckNameLength() {
        EmployeeDto employeeDtoForSave = getEmployeeDtoForSaveInController();
        employeeDtoForSave.setName(SHORT_NAME);
        String checkMessage = employeeChecker.checkNameLength(employeeDtoForSave);
        assertThat(checkMessage).isNotNull().isEqualTo("validation.name.length");
    }

    @Test
    void shouldCheckSalary() {
        EmployeeDto employeeDtoForSave = getEmployeeDtoForSaveInController();
        employeeDtoForSave.setSalary(ZERO_SALARY);
        String checkMessage = employeeChecker.checkSalary(employeeDtoForSave);
        assertThat(checkMessage).isNotNull().isEqualTo("validation.salary.size");
    }

    @Test
    void shouldCheckNameByNull() {
        EmployeeDto employeeDtoForSave = getEmployeeDtoForSaveInController();
        employeeDtoForSave.setName(null);
        String checkMessage = employeeChecker.checkNameByNull(employeeDtoForSave);
        assertThat(checkMessage).isNotNull().isEqualTo("name validation.not.null");
    }
}