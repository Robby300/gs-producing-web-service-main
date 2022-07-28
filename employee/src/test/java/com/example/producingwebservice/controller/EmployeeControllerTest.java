package com.example.producingwebservice.controller;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.model.EmployeeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.producingwebservice.testData.EmployeeTestData.*;
import static com.example.producingwebservice.type.ResponseStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class EmployeeControllerTest extends IntegrationTestBase {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final int ZERO_INDEX = 0;
    public static final int FIRST_INDEX = 1;
    private final EmployeeController controller;

    @Autowired
    EmployeeControllerTest(EmployeeController controller) {
        this.controller = controller;
    }

    @Test
    void findAll() {
        List<EmployeeDto> all = controller.findAll();
        assertThat(all).contains(
                getFirstEmployeeDto(),
                getSecondEmployeeDto(),
                getThirdEmployeeDto());
    }

    @Test
    void saveAll() {
        ResponseEntity<List<EmployeeResponse>> listResponseEntity = controller.saveAll(getEmployeeDtosForSaveInController());
        assertThat(listResponseEntity.getBody().get(ZERO_INDEX).getResponseStatus()).isEqualTo(SUCCESS);
        assertThat(listResponseEntity.getBody().get(FIRST_INDEX).getResponseStatus()).isEqualTo(SUCCESS);
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
        String contentType = employeePdfResponseEntity.getHeaders().get(CONTENT_TYPE).get(ZERO_INDEX);
        assertThat(contentType).isEqualTo(APPLICATION_PDF);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void assignTask() {
    }

    @Test
    void unAssignTask() {
    }
}