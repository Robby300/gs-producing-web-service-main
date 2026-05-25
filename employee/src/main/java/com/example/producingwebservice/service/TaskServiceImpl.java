package com.example.producingwebservice.service;

import com.example.producingwebservice.api.TaskService;
import com.example.producingwebservice.entity.Task;
import com.example.producingwebservice.exception.TaskNotFoundException;
import com.example.producingwebservice.model.TaskDto;
import com.example.producingwebservice.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

	private static final String ID_NOT_FOUND_MESSAGE = "Id not found";
	private static final String TASK_CACHE_PREFIX = "task:";
	private static final String TASK_ALL_CACHE = "tasks:all";
	private static final long CACHE_TTL_HOURS = 1;
	private final TaskRepository taskRepository;
	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public List<TaskDto> findAll() {
		String cached = stringRedisTemplate.opsForValue().get(TASK_ALL_CACHE);
		if (cached != null) {
			try {
				return objectMapper.readValue(cached, new TypeReference<List<TaskDto>>() {});
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cached task list", e);
			}
		}

		List<TaskDto> result = taskRepository.findAll().stream()
				.map(this::toDto)
				.collect(Collectors.toList());

		try {
			stringRedisTemplate.opsForValue().set(
					TASK_ALL_CACHE, objectMapper.writeValueAsString(result),
					CACHE_TTL_HOURS, TimeUnit.HOURS);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize task list for cache", e);
		}
		return result;
	}

	@Override
	public TaskDto findById(Long id) {
		String cached = stringRedisTemplate.opsForValue().get(TASK_CACHE_PREFIX + id);
		if (cached != null) {
			try {
				return objectMapper.readValue(cached, TaskDto.class);
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cached task {}", id, e);
			}
		}

		Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(ID_NOT_FOUND_MESSAGE));
		TaskDto dto = toDto(task);

		try {
			stringRedisTemplate.opsForValue().set(
					TASK_CACHE_PREFIX + id, objectMapper.writeValueAsString(dto),
					CACHE_TTL_HOURS, TimeUnit.HOURS);
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize task {} for cache", id, e);
		}
		return dto;
	}

	@Override
	public TaskDto update(Long id, TaskDto taskDto) {
		TaskDto taskDtoFromRepo = findById(id);
		taskDtoFromRepo.setDescription(taskDto.getDescription());
		evictCache(id);
		return save(taskDtoFromRepo);
	}

	@Override
	public void deleteById(Long id) {
		taskRepository.deleteById(id);
		evictCache(id);
	}

	@Override
	public TaskDto save(TaskDto taskDto) {
		Task task = new Task(taskDto.getId(), taskDto.getDescription());
		taskRepository.save(task);
		evictCache(task.getId());
		return taskDto;
	}

	@Override
	public List<TaskDto> saveAll(List<TaskDto> taskDtos) {
		return taskDtos.stream().map(this::save).collect(Collectors.toList());
	}

	private void evictCache(Long id) {
		stringRedisTemplate.delete(TASK_CACHE_PREFIX + id);
		stringRedisTemplate.delete(TASK_ALL_CACHE);
	}

	private TaskDto toDto(Task task) {
		return new TaskDto(task.getId(), task.getDescription());
	}
}
