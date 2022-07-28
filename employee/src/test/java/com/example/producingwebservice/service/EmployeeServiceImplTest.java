package com.example.producingwebservice.service;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import com.example.producingwebservice.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static com.example.producingwebservice.testData.TaskTestData.getThirdTask;
import static com.example.producingwebservice.type.Position.DIRECTOR;
import static com.example.producingwebservice.type.Position.WORKER;
import static com.example.producingwebservice.type.ResponseStatus.FAILURE;
import static com.example.producingwebservice.type.ResponseStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class EmployeeServiceImplTest extends IntegrationTestBase {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    EmployeeServiceImplTest(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @Test
    void getEmployeePdfResponseEntity() {
        ResponseEntity<InputStreamResource> employeePdfResponseEntity =
                employeeService.getEmployeePdfResponseEntity(getFirstEmployeeDto().getUuid());
        String contentType = Objects.requireNonNull(employeePdfResponseEntity.getHeaders().get("Content-Type")).get(0);
        assertThat(contentType).isEqualTo("application/pdf");
    }

    @Test
    @Transactional
    void shouldFindAll() {
        List<EmployeeDto> all = employeeService.findAll();
        assertThat(all).contains(
                getFirstEmployeeDto(),
                getSecondEmployeeDto(),
                getThirdEmployeeDto());
    }

    @Test
    void deleteByUuid() {
        EmployeeDto existedEmployee = employeeService.findByUuid("fourthUUID");
        assertThat(existedEmployee).isNotNull();
        employeeService.deleteByUuid("fourthUUID");
        assertThatThrownBy(() -> employeeService.findByUuid("fourthUUID"))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void shouldGetSuccessStatusWhenValidSave() {
        EmployeeResponse saveResponse = employeeService.save(getEmployeeDtoForSave());
        assertThat(saveResponse.getResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void ShouldFindByUuid() {
        EmployeeDto foundByUuid = employeeService.findByUuid(getSecondEmployeeDto().getUuid());
        assertThat(foundByUuid).isEqualTo(getSecondEmployeeDto());
    }

    @Test
    void shouldSaveAll() {
        List<EmployeeResponse> employeeResponses = employeeService.saveAll(getEmployeeDtosForSave());
        assertThat(employeeResponses.get(0).getResponseStatus()).isEqualTo(SUCCESS);
        assertThat(employeeResponses.get(1).getResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void shouldGetSuccessStatusWhenValidUpdate() {
        EmployeeDto employeeForSave = getFourthEmployeeDto();
        employeeForSave.setName("Ivan");
        employeeForSave.setPosition(WORKER);
        employeeForSave.setSalary("51000");
        EmployeeResponse updateResponse = employeeService.update(employeeForSave.getUuid(), employeeForSave);
        assertThat(updateResponse.getResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void shouldGetFailureStatusWhenUpdateWithWrongSalary() {
        EmployeeDto employeeForSave = getFourthEmployeeDto();
        employeeForSave.setName("Ivan");
        employeeForSave.setPosition(WORKER);
        employeeForSave.setSalary("40000");
        EmployeeResponse updateResponse = employeeService.update(employeeForSave.getUuid(), employeeForSave);
        assertThat(updateResponse.getResponseStatus()).isEqualTo(FAILURE);
    }

    @Test
    void shouldGetFailureStatusWhenUpdateWithShortName() {
        EmployeeDto employeeForUpdate = getFourthEmployeeDto();
        employeeForUpdate.setName("Iv");
        employeeForUpdate.setPosition(WORKER);
        employeeForUpdate.setSalary("51000");
        EmployeeResponse updateResponse = employeeService.update(employeeForUpdate.getUuid(), employeeForUpdate);
        assertThat(updateResponse.getResponseStatus()).isEqualTo(FAILURE);
        EmployeeDto foundUpdatedEmployee = employeeService.findByUuid(employeeForUpdate.getUuid());
        assertThat(foundUpdatedEmployee.getName()).isEqualTo("fourth test employee");
        assertThat(foundUpdatedEmployee.getPosition()).isEqualTo(DIRECTOR);
        assertThat(foundUpdatedEmployee.getSalary()).isEqualTo("155000");
    }

    @Test
    void assignTaskToEmployee() {
        EmployeeResponse employeeResponse = employeeService.assignTaskToEmployee(
                getThirdEmployeeDto().getUuid(),
                getThirdTask().getId());
        assertThat(employeeResponse.getResponseStatus()).isEqualTo(SUCCESS);
    }
}