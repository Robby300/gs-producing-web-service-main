package com.example.producingwebservice.testData;

import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.model.EmployeeDto;
import com.example.producingwebservice.type.Position;
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
	private static final String LONG_NAME =
			"Ahmed Ibn Hamid All Rasul Fatih Ali Babaevich Ibn Fadlan";
	public static final int ZERO_INDEX = 0;
	public static final String LOW_SALARY = "40000";
	public static final String SALARY = "51000";
	public static final String NAME = "Ivan";
	public static final long TASK_ID = 1L;
	private static final ModelMapper modelMapper = new ModelMapper();

	public static EmployeeDto getFirstEmployeeDto() {
		return getTestEmployee("firstUUID", "first test employee", "55000", WORKER);
	}

	public static EmployeeDto getSecondEmployeeDto() {
		return getTestEmployee("secondUUID", "second test employee", "105000", MANAGER);
	}

	public static EmployeeDto getThirdEmployeeDto() {
		return getTestEmployee("thirdUUID", "third test employee", "155000", DIRECTOR);
	}

	public static EmployeeDto getEmployeeDtoForDeleteInService() {
		return getTestEmployee(
				"serviceDeleteUUID", "test employee for delete in service", "155000", DIRECTOR);
	}

	public static EmployeeDto getEmployeeDtoForDeleteInController() {
		return getTestEmployee(
				"controllerDeleteUUID",
				"test employee for delete in controller",
				"105000",
				MANAGER);
	}

	public static EmployeeDto getEmployeeDtoForUpdateInService() {
		return getTestEmployee(
				"serviceUpdateUUID", "test employee for update in service", "105000", MANAGER);
	}

	public static EmployeeDto getEmployeeDtoForUpdateInController() {
		return getTestEmployee(
				"controllerUpdateUUID", "test employee for update in controller", "55000", WORKER);
	}

	public static EmployeeDto getEmployeeDtoForSaveInService() {
		return getTestEmployee(
				"serviceSaveUUID", "test employee for save in service", "105000", MANAGER);
	}

	public static List<EmployeeDto> getEmployeeDtosForSaveInController() {
		return List.of(
				getTestEmployee("seventhUUID", "seventh test employee", "55000", WORKER),
				getTestEmployee("eighthUUID", "eighth test employee", "55000", WORKER));
	}

	public static List<EmployeeDto> getEmployeeDtosForSaveInService() {
		return List.of(
				getTestEmployee("fifthUUID", "fifth test employee", "55000", WORKER),
				getTestEmployee("sixthUUID", "sixth test employee", "55000", WORKER));
	}

	public static EmployeeDto getEmployeeDtoWithBigSalary() {
		return getTestEmployee(
				"bigSalaryUUID", "test employee for check big salary", ONE_MILLION_SALARY, WORKER);
	}

	public static EmployeeDto getEmployeeDtoWithManyTasks() {
		return new EmployeeDto(
				null,
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
		return getTestEmployee(
				"nullPositionUUID", "test employee with null position", "55000", null);
	}

	public static EmployeeDto getEmployeeDtoWithShortName() {
		return getTestEmployee("shortNameUUID", SHORT_NAME, "55000", WORKER);
	}

	public static EmployeeDto getEmployeeDtoWithZeroSalary() {
		return getTestEmployee(
				"zeroSalaryUUID", "test employee with zero salary", ZERO_SALARY, WORKER);
	}

	public static EmployeeDto getEmployeeDtoWithNullName() {
		return getTestEmployee("nullNameUUID", null, "55000", WORKER);
	}

	public static EmployeeDto getEmployeeDtoWithLongName() {
		return getTestEmployee("nullNameUUID", LONG_NAME, "55000", WORKER);
	}

	private static EmployeeDto getTestEmployee(
			String uuid, String name, String salary, Position position) {
		return new EmployeeDto(null, uuid, name, salary, position, Collections.emptySet());
	}
}
