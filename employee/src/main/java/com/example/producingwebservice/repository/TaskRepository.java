package com.example.producingwebservice.repository;


import com.example.producingwebservice.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
	@Override
	List<Task> findAll();

	void deleteById(Long id);
}
