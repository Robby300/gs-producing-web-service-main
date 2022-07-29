package com.example.producingwebservice.service;


import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.exception.TaskNotFoundException;
import com.example.producingwebservice.model.TaskDto;
import com.example.producingwebservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

	private static final String ID_NOT_FOUND_MESSAGE = "Id not found";
	private final TaskRepository taskRepository;
	private final ModelMapper modelMapper = new ModelMapper();

	@Override
	public List<TaskDto> findAll() {
		return taskRepository.findAll().stream()
				.map(task -> modelMapper.map(task, TaskDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public TaskDto findById(Long id) {
		Task task =
				taskRepository
						.findById(id)
						.orElseThrow(() -> new TaskNotFoundException(ID_NOT_FOUND_MESSAGE));
		return modelMapper.map(task, TaskDto.class);
	}

	@Override
	public TaskDto update(Long id, TaskDto taskDto) {
		TaskDto taskDtoFromRepo = findById(id);
		BeanUtils.copyProperties(taskDto, taskDtoFromRepo, "id");
		return save(taskDtoFromRepo);
	}

	@Override
	public void deleteById(Long id) {
		taskRepository.deleteById(id);
	}

	@Override
	public TaskDto save(TaskDto taskDto) {
		taskRepository.save(modelMapper.map(taskDto, Task.class));
		return taskDto;
	}

	@Override
	public List<TaskDto> saveAll(List<TaskDto> taskDtos) {
		return taskDtos.stream().map(this::save).collect(Collectors.toList());
	}
}
