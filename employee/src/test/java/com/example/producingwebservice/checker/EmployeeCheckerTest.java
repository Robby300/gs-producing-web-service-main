package com.example.producingwebservice.checker;

import com.example.producingwebservice.service.MessageService;
import com.example.producingwebservice.service.validator.EmployeeNotValidMessageService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EmployeeCheckerTest {
    private final EmployeeChecker employeeChecker = new EmployeeChecker(
            new EmployeeNotValidMessageService(
                    new MessageService(
                            new ResourceBundleMessageSource())));

    ModelMapper modelMapper = new ModelMapper();

    @Test
    void shouldCheckSalaryByPosition() {
        String checkMessage = employeeChecker.checkSalaryByPosition(getEmployeeDtoWithBigSalary());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("validation.position.salary");
    }

    @Test
    void shouldCheckCountOfTasks() {
        String checkMessage = employeeChecker.checkCountOfTasks(getEmployeeDtoWithManyTasks());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("validation.position.tasks");
    }

    @Test
    void shouldCheckPositionByNull() {
        String checkMessage = employeeChecker.checkPositionByNull(getEmployeeDtoWithNullPosition());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("position validation.not.null");
    }

    @Test
    void shouldCheckNameLength() {
        String checkMessage = employeeChecker.checkNameLength(getEmployeeDtoWithShortName());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("validation.name.length");
    }

    @Test
    void shouldCheckZeroSalary() {
        String checkMessage = employeeChecker.checkSalary(getEmployeeDtoWithZeroSalary());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("validation.salary.size");
    }

    @Test
    void shouldCheckNameByNull() {
        String checkMessage = employeeChecker.checkNameByNull(getEmployeeDtoWithNullName());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("name validation.not.null");
    }

    @Test
    void shouldCheckLongName() {
        String checkMessage = employeeChecker.checkNameLength(getEmployeeDtoWithLongName());
        assertThat(checkMessage)
                .isNotNull()
                .isEqualTo("validation.name.length");
    }
}