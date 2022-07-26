package com.example.producingwebservice.service;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        assertThat(all.size()).isEqualTo(12);
    }

    @Test
    void deleteByUuid() {
    }

    @Test
    void save() {
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