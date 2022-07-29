package com.example.producingwebservice.controller;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.exception.EmployeeNotFoundException;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static com.example.producingwebservice.type.ResponseStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class EmployeeControllerTest extends IntegrationTestBase {

	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final int ZERO_INDEX = 0;
	public static final int FIRST_INDEX = 1;
	public static final String ANOTHER_NAME = "Another Name";
	private final EmployeeController controller;

	@Autowired
	EmployeeControllerTest(EmployeeController controller) {
		this.controller = controller;
	}

	@Test
	void findAll() {
		List<EmployeeDto> all = controller.findAll();
		assertThat(all)
				.contains(getFirstEmployeeDto(), getSecondEmployeeDto(), getThirdEmployeeDto());
	}

	@Test
	void saveAll() {
		ResponseEntity<List<EmployeeResponse>> listResponseEntity =
				controller.saveAll(getEmployeeDtosForSaveInController());
		assertThat(
						Objects.requireNonNull(listResponseEntity.getBody())
								.get(ZERO_INDEX)
								.getResponseStatus())
				.isEqualTo(SUCCESS);
		assertThat(listResponseEntity.getBody().get(FIRST_INDEX).getResponseStatus())
				.isEqualTo(SUCCESS);
	}

	@Test
	void getByUuid() {
		EmployeeDto foundByUuid = controller.getByUuid(getSecondEmployeeDto().getUuid());
		assertThat(foundByUuid).isEqualTo(getSecondEmployeeDto());
	}

	@Test
	void getPdfByUuid() {
		ResponseEntity<InputStreamResource> employeePdfResponseEntity =
				controller.getPdfByUuid(getThirdEmployeeDto().getUuid());
		String contentType =
				Objects.requireNonNull(employeePdfResponseEntity.getHeaders().get(CONTENT_TYPE))
						.get(ZERO_INDEX);
		assertThat(contentType).isEqualTo(APPLICATION_PDF);
	}

	@Test
	void update() {
		EmployeeDto employeeForSave = getEmployeeDtoForUpdateInController();
		employeeForSave.setName(ANOTHER_NAME);
		EmployeeResponse updateResponse =
				controller.update(employeeForSave.getUuid(), employeeForSave);
		assertThat(updateResponse.getResponseStatus()).isEqualTo(SUCCESS);
	}

	@Test
	void delete() {
		String employeeForDeleteUuid = getEmployeeDtoForDeleteInController().getUuid();
		EmployeeDto existedEmployee = controller.getByUuid(employeeForDeleteUuid);
		assertThat(existedEmployee).isNotNull();
		controller.delete(employeeForDeleteUuid);
		assertThatThrownBy(() -> controller.getByUuid(employeeForDeleteUuid))
				.isInstanceOf(EmployeeNotFoundException.class);
	}
}
