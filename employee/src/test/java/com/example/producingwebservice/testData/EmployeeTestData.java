package com.example.producingwebservice.testData;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Set;

import static com.example.producingwebservice.testData.TaskTestData.getSecondTask;
import static com.example.producingwebservice.testData.TaskTestData.getThirdTask;
import static com.example.producingwebservice.type.Position.*;

public class EmployeeTestData {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static EmployeeDto getFirstEmployeeDto() {
        return new EmployeeDto(1,
                "firstUUID",
                "first test employee",
                "55000",
                WORKER,
                Set.of(modelMapper.map(getSecondTask(), Task.class),
                        modelMapper.map(getThirdTask(), Task.class)));
    }

    public static EmployeeDto getSecondEmployeeDto() {
        return new EmployeeDto(2,
                "secondUUID",
                "second test employee",
                "105000",
                MANAGER,
                Collections.emptySet());
    }

    public static EmployeeDto getThirdEmployeeDto() {
        return new EmployeeDto(3,
                "thirdUUID",
                "third test employee",
                "155000",
                DIRECTOR,
                Set.of(modelMapper.map(getFirstEmployeeDto(), Task.class)));
    }

    public static EmployeeDto getEmployeeDtoForSave() {
        return new EmployeeDto(4,
                "fourthUUID",
                "fourth test employee",
                "156000",
                DIRECTOR,
                Collections.emptySet());
    }
}
