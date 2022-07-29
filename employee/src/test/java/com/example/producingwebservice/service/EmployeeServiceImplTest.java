package com.example.producingwebservice.service;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.api.EmployeeService;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import org.junit.jupiter.api.Test;
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

	private final EmployeeService employeeService;

	@Autowired
	EmployeeServiceImplTest(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Test
	void shouldGetEmployeePdfResponseEntity() {
		ResponseEntity<InputStreamResource> employeePdfResponseEntity =
				employeeService.getEmployeePdfResponseEntity(getSecondEmployeeDto().getUuid());
		String contentType =
				Objects.requireNonNull(employeePdfResponseEntity.getHeaders().get(CONTENT_TYPE))
						.get(ZERO_INDEX);
		assertThat(contentType).isEqualTo(APPLICATION_PDF);
	}

	@Test
	void shouldFindAll() {
		List<EmployeeDto> all = employeeService.findAll();
		assertThat(all)
				.contains(getFirstEmployeeDto(), getSecondEmployeeDto(), getThirdEmployeeDto());
	}

	@Test
	void shouldGetSuccessStatusWhenValidSave() {
		EmployeeResponse saveResponse = employeeService.save(getEmployeeDtoForSaveInService());
		assertThat(saveResponse.getResponseStatus()).isEqualTo(SUCCESS);
	}

	@Test
	void ShouldFindByUuid() {
		EmployeeDto foundByUuid = employeeService.findByUuid(getSecondEmployeeDto().getUuid());
		assertThat(foundByUuid).isEqualTo(getSecondEmployeeDto());
	}

	@Test
	void shouldSaveAll() {
		List<EmployeeResponse> employeeResponses =
				employeeService.saveAll(getEmployeeDtosForSaveInService());
		assertThat(employeeResponses.get(0).getResponseStatus()).isEqualTo(SUCCESS);
		assertThat(employeeResponses.get(1).getResponseStatus()).isEqualTo(SUCCESS);
	}

	@Test
	void shouldGetSuccessStatusWhenValidUpdate() {
		EmployeeDto employeeForUpdate = getEmployeeDtoForUpdateInService();
		employeeForUpdate.setName(NAME);
		employeeForUpdate.setPosition(WORKER);
		employeeForUpdate.setSalary(SALARY);
		EmployeeResponse updateResponse =
				employeeService.update(employeeForUpdate.getUuid(), employeeForUpdate);
		assertThat(updateResponse.getResponseStatus()).isEqualTo(SUCCESS);
	}

	@Test
	void shouldGetFailureStatusWhenUpdateWithWrongSalary() {
		EmployeeDto employeeForUpdate = getEmployeeDtoForUpdateInService();
		employeeForUpdate.setPosition(WORKER);
		employeeForUpdate.setSalary(LOW_SALARY);
		EmployeeResponse updateResponse =
				employeeService.update(employeeForUpdate.getUuid(), employeeForUpdate);
		assertThat(updateResponse.getResponseStatus()).isEqualTo(FAILURE);
	}

	@Test
	void shouldGetFailureStatusWhenUpdateWithShortName() {
		EmployeeDto employeeForUpdate = getEmployeeDtoForUpdateInService();
		employeeForUpdate.setName(SHORT_NAME);
		EmployeeResponse updateResponse =
				employeeService.update(employeeForUpdate.getUuid(), employeeForUpdate);
		assertThat(updateResponse.getResponseStatus()).isEqualTo(FAILURE);
		EmployeeDto foundUpdatedEmployee = employeeService.findByUuid(employeeForUpdate.getUuid());
		assertThat(foundUpdatedEmployee).isNotEqualTo(employeeForUpdate);
	}

	@Test
	void shouldDeleteByUuid() {
		String employeeForDeleteUuid = getEmployeeDtoForDeleteInService().getUuid();
		EmployeeDto existedEmployee = employeeService.findByUuid(employeeForDeleteUuid);
		assertThat(existedEmployee).isNotNull();
		employeeService.deleteByUuid(employeeForDeleteUuid);
		assertThatThrownBy(() -> employeeService.findByUuid(employeeForDeleteUuid))
				.isInstanceOf(EmployeeNotFoundException.class);
	}

	@Test
	void shouldAssignTaskToEmployee() {
		assertThat(employeeService.findByUuid(getFirstEmployeeDto().getUuid()).getTasks())
				.isEmpty();
		EmployeeResponse employeeResponse =
				employeeService.assignTaskToEmployee(getFirstEmployeeDto().getUuid(), TASK_ID);
		assertThat(employeeResponse.getResponseStatus()).isEqualTo(SUCCESS);
	}
}
