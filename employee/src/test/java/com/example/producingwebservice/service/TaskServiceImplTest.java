package com.example.producingwebservice.service;

import com.example.producingwebservice.IntegrationTestBase;
import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.exception.TaskNotFoundException;
import com.example.producingwebservice.model.TaskDto;
import com.example.producingwebservice.repository.TaskRepository;
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
    private final TaskRepository taskRepository;

    @Autowired
    TaskServiceImplTest(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @Test
    void shouldFindAll() {
        List<TaskDto> all = taskService.findAll();
        assertThat(all).contains(
                getThirdTask(),
                getSecondTask(),
                getThirdTask());
    }

    @Test
    void findById() {
        TaskDto foundTask = taskService.findById(16L);
        assertThat(foundTask).isEqualTo(getFirstTask());
    }

    @Test
    void update() {
        TaskDto taskForUpdate = taskService.findById(17L);
        taskForUpdate.setDescription("updated task");
        taskService.update(17L, taskForUpdate);
        assertThat(taskService.findById(17L).getDescription()).isEqualTo("updated task");
    }

    @Test
    void deleteById() {
        taskService.deleteById(18L);
        assertThatThrownBy(() -> taskService.findById(18L)).isInstanceOf(TaskNotFoundException.class);
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