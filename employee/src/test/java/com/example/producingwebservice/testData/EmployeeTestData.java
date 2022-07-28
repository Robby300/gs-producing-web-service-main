package com.example.producingwebservice.testData;

import com.example.producingwebservice.model.EmployeeDto;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static com.example.producingwebservice.type.Position.*;

public class EmployeeTestData {

    private static final ModelMapper modelMapper = new ModelMapper();

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

    public static EmployeeDto getFourthEmployeeDto() {
        return new EmployeeDto(null,
                "fourthUUID",
                "fourth test employee",
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForSave() {
        return new EmployeeDto(null,
                "forSaveUUID",
                "forSave test employee",
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoForDelete() {
        return new EmployeeDto(null,
                "forDeleteUUID",
                "test employee for delete",
                "155000",
                DIRECTOR,
                Collections.emptySet());
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
}