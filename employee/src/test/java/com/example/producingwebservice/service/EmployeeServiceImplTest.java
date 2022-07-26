package com.example.producingwebservice.service;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import com.example.producingwebservice.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmployeeServiceImplTest extends IntegrationTestBase {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    EmployeeServiceImplTest(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getEmployeePdfResponseEntity() {
    }

    @Test
    void findAll() {
        List<EmployeeDto> all = employeeService.findAll();
        assertThat(all).hasSize(3);
        assertThat(all).contains(
                getFirstEmployeeDto(),
                getSecondEmployeeDto(),
                getThirdEmployeeDto());
    }

    @Test
    void deleteByUuid() {
        EmployeeDto existedEmployee = employeeService.findByUuid("secondUUID");
        assertThat(existedEmployee).isNotNull();
        employeeService.deleteByUuid("secondUUID");
        assertThatThrownBy(() -> employeeService.findByUuid("secondUUID"))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void save() {
        EmployeeResponse savedEmployee = employeeService.save(getEmployeeDtoForSave());
        assertThat(employeeService.findByUuid(getEmployeeDtoForSave().getUuid())).isEqualTo(savedEmployee);
    }

    @Test
    void findByUuid() {
    }

    @Test
    void saveAll() {
    }

    @Test
    void update() {
    }

    @Test
    void assignTaskToEmployee() {
    }

    @Test
    void unAssignTaskFromEmployee() {
    }
}