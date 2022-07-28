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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static com.example.producingwebservice.type.Position.WORKER;
import static com.example.producingwebservice.type.ResponseStatus.FAILURE;
import static com.example.producingwebservice.type.ResponseStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
class EmployeeServiceImplTest extends IntegrationTestBase {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final int ZERO_INDEX = 0;
    public static final String SHORT_NAME = "Iv";
    public static final String LOW_SALARY = "40000";
    public static final String SALARY = "51000";
    public static final String NAME = "Ivan";
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    EmployeeServiceImplTest(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @Test
    void shouldGetEmployeePdfResponseEntity() {
        ResponseEntity<InputStreamResource> employeePdfResponseEntity =
                employeeService.getEmployeePdfResponseEntity(getFirstEmployeeDto().getUuid());
        String contentType = Objects.requireNonNull(employeePdfResponseEntity.getHeaders().get(CONTENT_TYPE)).get(ZERO_INDEX);
        assertThat(contentType).isEqualTo(APPLICATION_PDF);
    }

    @Test
    void shouldFindAll() {
        List<EmployeeDto> all = employeeService.findAll();
        assertThat(all).contains(
                getFirstEmployeeDto(),
                getSecondEmployeeDto(),
                getThirdEmployeeDto());
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
        List<EmployeeResponse> employeeResponses = employeeService.saveAll(getEmployeeDtosForSaveInService());
        assertThat(employeeResponses.get(0).getResponseStatus()).isEqualTo(SUCCESS);
        assertThat(employeeResponses.get(1).getResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void shouldGetSuccessStatusWhenValidUpdate() {
        EmployeeDto employeeForSave = getFourthEmployeeDto();
        employeeForSave.setName(NAME);
        employeeForSave.setPosition(WORKER);
        employeeForSave.setSalary(SALARY);
        EmployeeResponse updateResponse = employeeService.update(employeeForSave.getUuid(), employeeForSave);
        assertThat(updateResponse.getResponseStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void shouldGetFailureStatusWhenUpdateWithWrongSalary() {
        EmployeeDto employeeForSave = getFourthEmployeeDto();
        employeeForSave.setPosition(WORKER);
        employeeForSave.setSalary(LOW_SALARY);
        EmployeeResponse updateResponse = employeeService.update(employeeForSave.getUuid(), employeeForSave);
        assertThat(updateResponse.getResponseStatus()).isEqualTo(FAILURE);
    }

    @Test
    void shouldGetFailureStatusWhenUpdateWithShortName() {
        EmployeeDto employeeForUpdate = getFourthEmployeeDto();
        employeeForUpdate.setName(SHORT_NAME);
        EmployeeResponse updateResponse = employeeService.update(employeeForUpdate.getUuid(), employeeForUpdate);
        assertThat(updateResponse.getResponseStatus()).isEqualTo(FAILURE);
        EmployeeDto foundUpdatedEmployee = employeeService.findByUuid(employeeForUpdate.getUuid());
        assertThat(foundUpdatedEmployee).isNotEqualTo(employeeForUpdate);
    }

    @Test
    void shouldDeleteByUuid() {
        String employeeForDeleteUuid = getEmployeeDtoForDelete().getUuid();
        EmployeeDto existedEmployee = employeeService.findByUuid(employeeForDeleteUuid);
        assertThat(existedEmployee).isNotNull();
        employeeService.deleteByUuid(employeeForDeleteUuid);
        assertThatThrownBy(() -> employeeService.findByUuid(employeeForDeleteUuid))
                .isInstanceOf(EmployeeNotFoundException.class);
    }
}