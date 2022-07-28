package com.example.producingwebservice.testData;

import com.example.producingwebservice.model.EmployeeDto;

import java.util.Collections;
import java.util.List;

import static com.example.producingwebservice.type.Position.*;

public class EmployeeTestData {

    public static EmployeeDto getFirstEmployeeDto() {
        return new EmployeeDto(null,
                "firstUUID",
                "first test employee",
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getSecondEmployeeDto() {
        return new EmployeeDto(null,
                "secondUUID",
                "second test employee",
                "105000",
                MANAGER,
                Collections.emptySet());
    }

    public static EmployeeDto getThirdEmployeeDto() {
        return new EmployeeDto(null,
                "thirdUUID",
                "third test employee",
                "155000",
                DIRECTOR,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForDeleteInService() {
        return new EmployeeDto(null,
                "serviceDeleteUUID",
                "test employee for delete in service",
                "155000",
                DIRECTOR,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForDeleteInController() {
        return new EmployeeDto(null,
                "controllerDeleteUUID",
                "test employee for delete in controller",
                "105000",
                MANAGER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForUpdateInService() {
        return new EmployeeDto(null,
                "serviceUpdateUUID",
                "test employee for update in service",
                "105000",
                MANAGER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForUpdateInController() {
        return new EmployeeDto(null,
                "controllerUpdateUUID",
                "test employee for update in controller",
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForSaveInService() {
        return new EmployeeDto(null,
                "serviceSaveUUID",
                "test employee for save in service",
                "105000",
                MANAGER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForSaveInController() {
        return new EmployeeDto(null,
                "controllerSaveUUID",
                "test employee for save in controller",
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static List<EmployeeDto> getEmployeeDtosForSaveInController() {
        return List.of(
                new EmployeeDto(null,
                        "seventhUUID",
                        "seventh test employee",
                        "55000",
                        WORKER,
                        Collections.emptySet()),
                new EmployeeDto(null,
                        "eighthUUID",
                        "eighth test employee",
                        "55000",
                        WORKER,
                        Collections.emptySet()));
    }

    public static List<EmployeeDto> getEmployeeDtosForSaveInService() {
        return List.of(
                new EmployeeDto(null,
                        "fifthUUID",
                        "fifth test employee",
                        "55000",
                        WORKER,
                        Collections.emptySet()),
                new EmployeeDto(null,
                        "sixthUUID",
                        "sixth test employee",
                        "55000",
                        WORKER,
                        Collections.emptySet()));
    }
}
