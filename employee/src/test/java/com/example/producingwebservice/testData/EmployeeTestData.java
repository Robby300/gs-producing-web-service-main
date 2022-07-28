package com.example.producingwebservice.testData;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.example.producingwebservice.testData.TaskTestData.*;
import static com.example.producingwebservice.type.Position.*;

public class EmployeeTestData {
    public static final String ONE_MILLION_SALARY = "1000000";
    public static final String ZERO_SALARY = "0";
    public static final String SHORT_NAME = "ab";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_PDF = "application/pdf";
    private static final String LONG_NAME = "Ahmed Ibn Hamid All Rasul Fatih Ali Babaevich Ibn Fadlan";
    public static final int ZERO_INDEX = 0;
    public static final String LOW_SALARY = "40000";
    public static final String SALARY = "51000";
    public static final String NAME = "Ivan";
    public static final long TASK_ID = 1L;
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

    public static EmployeeDto getEmployeeDtoWithBigSalary() {
        return new EmployeeDto(null,
                "bigSalaryUUID",
                "test employee for check big salary",
                ONE_MILLION_SALARY,
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoWithManyTasks() {
        return new EmployeeDto(null,
                "manyTasksUUID",
                "test employee with many tasks",
                "55000",
                WORKER,
                Set.of(
                        modelMapper.map(getFirstTask(), Task.class),
                        modelMapper.map(getSecondTask(), Task.class),
                        modelMapper.map(getThirdTask(), Task.class)));
    }

    public static EmployeeDto getEmployeeDtoWithNullPosition() {
        return new EmployeeDto(null,
                "nullPositionUUID",
                "test employee with null position",
                "55000",
                null,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoWithShortName() {
        return new EmployeeDto(null,
                "shortNameUUID",
                SHORT_NAME,
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoWithZeroSalary() {
        return new EmployeeDto(null,
                "zeroSalaryUUID",
                "test employee with zero salary",
                ZERO_SALARY,
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoWithNullName() {
        return new EmployeeDto(null,
                "nullNameUUID",
                null,
                "55000",
                WORKER,
                Collections.emptySet());
    }

    public static EmployeeDto getEmployeeDtoWithLongName() {
        return new EmployeeDto(null,
                "nullNameUUID",
                LONG_NAME,
                "55000",
                WORKER,
                Collections.emptySet());
    }
}
