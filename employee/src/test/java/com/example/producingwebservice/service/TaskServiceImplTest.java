package com.example.producingwebservice.service;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.exception.TaskNotFoundException;
import com.example.producingwebservice.model.TaskDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.producingwebservice.testData.TaskTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
class TaskServiceImplTest extends IntegrationTestBase {
	private final TaskService taskService;

	@Autowired
	TaskServiceImplTest(TaskService taskService) {
		this.taskService = taskService;
	}

	@Test
	void shouldFindAll() {
		List<TaskDto> all = taskService.findAll();
		assertThat(all).contains(getThirdTask(), getSecondTask(), getThirdTask());
	}

	@Test
	void findById() {
		TaskDto foundTask = taskService.findById(1L);
		assertThat(foundTask).isNotNull();
	}

	@Test
	void update() {
		TaskDto taskForUpdate = getThirdTask();
		taskForUpdate.setDescription(UPDATED_DESCRIPTION);
		taskService.update(1L, taskForUpdate);
		assertThat(taskService.findById(1L).getDescription()).isEqualTo(UPDATED_DESCRIPTION);
	}

	@Test
	void deleteById() {
		taskService.deleteById(2L);
		assertThatThrownBy(() -> taskService.findById(2L))
				.isInstanceOf(TaskNotFoundException.class);
	}

	@Test
	void save() {
		TaskDto savedTask = taskService.save(getFourthTask());
		assertThat(savedTask).isNotNull();
	}

	@Test
	void saveAll() {
		List<TaskDto> taskDtos = taskService.saveAll(getTaskDtoList());
		assertThat(taskDtos).isNotNull();
	}
}
